package com.receiptorc.service;

import com.receiptorc.dto.ReceiptResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReceiptorcService {
    public ReceiptResponseDTO receiptorc(byte[] fileBytes) {
        System.out.println("Log: the application arrived in the service ");
        return null;
    }
}
