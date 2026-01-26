package com.receiptorc.controller;

import com.receiptorc.dto.ReceiptResponseDTO;
import com.receiptorc.exceptions.TechnicalException;
import com.receiptorc.service.ReceiptorcService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Valid
@RestController
@RequestMapping("/")
public class ReceiptorcControllerRest {
    @Autowired
    private Tika tika;

    @Autowired
    private ReceiptorcService receiptorcService;

    private static final Set<String> extensionsAllowed = Set.of(
            "image/png", "image/jpeg", "image/webp", "image/heic", "image/heif");

    /**
     * This function is a post mapping to upload the file
     * @param file is an archive with extension and size controlled to up receipt
     * @return a dto with response of compensation money
     * @throws IOException will catch any problem with extension of file
     */
    @PostMapping("/receipt")
    public ResponseEntity<ReceiptResponseDTO> uploadReceipt(
            @RequestParam("file") @NonNull MultipartFile file) throws IOException {
        validateFile(file); // Verify if file is valid
        byte[] fileBytes = file.getBytes();
        return ResponseEntity.ok(receiptorcService.receiptorc(fileBytes));
    }


    /**
     * This function valid the file param to ensure that it is not null or don't be a type allowed by system
     * @param file is the file uploaded by user
     * @throws TechnicalException will catch any problem with extension of file
     * @throws IOException if the file has any problem unknow
     */
    public void validateFile(MultipartFile file) throws TechnicalException, IOException {
        if (file.isEmpty()) {
            throw new TechnicalException("The file is empty.");
        }

        // Verify by Magic Bytes
        String mimeType;
        try (var is = file.getInputStream()) {
            mimeType = tika.detect(is);
        }

        if (!extensionsAllowed.contains(mimeType)) {
            throw new TechnicalException("The file type " + mimeType + " is not allowed.");
        }

        // structure Verify (ImageIO)
        try (var is = file.getInputStream()) {
            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                // if Tika detect type byt the ImageIo returns null,
                // the file has been a "false positive" malicious
                throw new TechnicalException("Security Alert: File signature matches " + mimeType + " but content is unreadable.");
            }

            // Protect pixel bombs
            if (image.getWidth() > 5000 || image.getHeight() > 5000) {
                throw new TechnicalException("Image dimensions are too large for OCR processing.");
            }

        } catch (IOException e) {
            throw new IOException("Error to process image");
        }
    }
}

