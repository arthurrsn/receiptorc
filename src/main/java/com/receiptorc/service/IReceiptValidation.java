package com.receiptorc.service;

import com.receiptorc.exceptions.TechnicalException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IReceiptValidation {
    void validateController(MultipartFile file) throws TechnicalException, IOException;
}
