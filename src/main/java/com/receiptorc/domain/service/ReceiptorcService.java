package com.receiptorc.domain.service;

import com.receiptorc.dto.ReceiptRequestDTO;
import com.receiptorc.dto.ReceiptResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReceiptorcService implements IReceiptorcService{

    @Value("${upload.directory}") // Value presents in application.properties is put here
    private String UPLOAD_DIR;

    @Override
    public ReceiptResponseDTO receiptorc(ReceiptRequestDTO receipt) {
        System.out.println("Log: the application arrived in the service ");
        return null;
    }
}
