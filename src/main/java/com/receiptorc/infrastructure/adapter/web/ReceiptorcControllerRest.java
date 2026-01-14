package com.receiptorc.infrastructure.adapter.web;


import com.receiptorc.domain.service.IReceiptorcService;
import com.receiptorc.dto.ReceiptRequestDTO;
import com.receiptorc.dto.ReceiptResponseDTO;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@Validated
@RestController
@RequestMapping("/")
public class ReceiptorcControllerRest {
    private final IReceiptorcService receiptorcService;

    // Builder
    @Autowired
    public ReceiptorcControllerRest(IReceiptorcService receiptorcService) {
        this.receiptorcService = receiptorcService;
    }

    private final List<String> extensionsAllowed = List.of("png", "jpeg");

    /**
     * This function is a post mapping to upload the file
     * @param file is an archive with extension and size controlled to up receipt
     * @return a dto with response of compensation money
     * @throws FileNotFoundException temporally
     * @throws FileUploadException temporally
     */
    @PostMapping("/receipt")
    public ResponseEntity<ReceiptResponseDTO> uploadReceipt(
            @RequestParam("file") MultipartFile file) throws FileNotFoundException, FileUploadException {

        if (file.isEmpty()){
            throw new FileNotFoundException("File cannot found"); // TODO: TEMPORALLY. EXCHANGE THE EXCEPTION FOR A HANDLER.
        }

        getExtension(file);
        ReceiptRequestDTO receipt = new ReceiptRequestDTO(file);

        return ResponseEntity.ok(receiptorcService.receiptorc(receipt));
    }


    /**
     * This function valid the file param to ensure that it is not null or don't be a type allowed by system
     * @param file is the file uploaded by user
     * @throws FileUploadException temporally
     */
    private void getExtension(MultipartFile file) throws FileUploadException {
        String contentType = file.getContentType(); // returns something kind image/png

        if (contentType == null || !contentType.contains("/")) {
            throw new FileUploadException("Invalid file type.");
        }

        String extension = contentType.split("/")[1].toLowerCase();
        if (!extensionsAllowed.contains(extension)) {
            throw new FileUploadException("Extension ." + extension + " is not allowed."); // TODO: TEMPORALLY. EXCHANGE THE EXCEPTION FOR A HANDLER.
        }
    }
}
