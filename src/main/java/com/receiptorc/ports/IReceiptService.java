package com.receiptorc.ports;

import com.receiptorc.dto.ReceiptResponseDTO;

import java.io.File;
import java.io.IOException;

public interface IReceiptService {
    ReceiptResponseDTO receiptorc(File fileImages) throws InterruptedException, IOException;
}
