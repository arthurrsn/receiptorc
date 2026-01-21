package com.receiptorc.infrastructure.adapter.web;

import com.receiptorc.domain.ports.IReceiptorcService;
import com.receiptorc.dto.ReceiptResponseDTO;
import com.receiptorc.infrastructure.exceptions.NotFoundException;
import com.receiptorc.infrastructure.exceptions.UploadException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Validated
@RestController
@RequestMapping("/")
public class ReceiptorcControllerRest {
    @Autowired
    private IReceiptorcService receiptorcService;

    private static final Set<String> extensionsAllowed = Set.of("png", "jpg");

    /**
     * This function is a post mapping to upload the file
     * @param file is an archive with extension and size controlled to up receipt
     * @return a dto with response of compensation money
     * @throws NotFoundException when the file doesn't found in system.
     * @throws UploadException will catch any problem with extension of file
     */
    @PostMapping("/receipt")
    public ResponseEntity<ReceiptResponseDTO> uploadReceipt(
            @RequestParam("file") MultipartFile file) throws NotFoundException, UploadException {

        if (file.isEmpty()){throw new NotFoundException("File not found.");}
        getExtension(file); // Verify if file is valid

        return ResponseEntity.ok(receiptorcService.receiptorc(file));
    }


    /**
     * This function valid the file param to ensure that it is not null or blank or don't be a type allowed by system
     * @param file is the file uploaded by user
     * @throws UploadException will catch any problem with extension of file
     */
    private static void getExtension(MultipartFile file) throws UploadException {
        // TODO: USE A DIFFERENT WAY TO VERIFY THE AUTHENTIFY OF THE FILE. SOMETHING KIND TIKA
        // SEE THIS ARTICLE: https://medium.com/@pvprasanth474/how-to-prevent-fake-file-uploads-in-java-detect-file-type-safely-d96acac68b9f
        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // file.jpg  -> extension = jpg
        if (extension == null || extension.isBlank()) {
            throw new UploadException("Invalid file type.");
        }

        if (!extensionsAllowed.contains(extension)) {
            throw new UploadException("Extension ." + extension + " is not allowed.");
        }
    }
}
