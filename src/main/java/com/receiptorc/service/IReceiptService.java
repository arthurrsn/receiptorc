package com.receiptorc.service;

import com.receiptorc.dto.ReceiptResponseDTO;

public interface IReceiptService {
    ReceiptResponseDTO receiptorc(byte[] fileBytes);
}
