package com.receiptorc.domain.service;

import com.receiptorc.dto.ReceiptRequestDTO;
import com.receiptorc.dto.ReceiptResponseDTO;

public interface IReceiptorcService {
    public ReceiptResponseDTO receiptorc(ReceiptRequestDTO receipt);
}
