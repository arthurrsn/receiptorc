package com.receiptorc.application.service;

import com.receiptorc.domain.ports.IReceiptorcService;
import com.receiptorc.dto.ReceiptResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReceiptorcService implements IReceiptorcService {
    @Override
    public ReceiptResponseDTO receiptorc(MultipartFile receipt) {
        System.out.println("Log: the application arrived in the service ");
        return null;
    }
}
