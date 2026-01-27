package com.receiptorc.controller;

import com.receiptorc.dto.ReceiptResponseDTO;
import com.receiptorc.service.IReceiptValidation;
import com.receiptorc.service.ReceiptValidation;
import com.receiptorc.service.ReceiptorcService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ReceiptorcControllerRest {
    private final ReceiptorcService receiptorcService;
    private final IReceiptValidation receiptValidation;

    public ReceiptorcControllerRest(ReceiptorcService receiptorcService, ReceiptValidation receiptValidation){
        this.receiptorcService = receiptorcService;
        this.receiptValidation = receiptValidation;
    }

    /**
     * This function is a post mapping to upload the file
     * @param file is an archive with extension and size controlled to up receipt
     * @return a dto with response of compensation money
     * @throws IOException will catch any problem with extension of file
     */
    @PostMapping("/receipt")
    public ResponseEntity<ReceiptResponseDTO> uploadReceipt(
            @RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        receiptValidation.validateController(file); // Verify if file is valid
        byte[] fileBytes = file.getBytes();
        return ResponseEntity.ok(receiptorcService.receiptorc(fileBytes));
    }


}

