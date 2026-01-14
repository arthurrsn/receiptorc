package com.receiptorc.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ReceiptRequestDTO(
        @NotNull
        MultipartFile file
) {
}
