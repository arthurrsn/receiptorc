package com.receiptorc.domain.ports;

import com.receiptorc.dto.ReceiptResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IReceiptorcService {
    ReceiptResponseDTO receiptorc(MultipartFile receipt);
}
