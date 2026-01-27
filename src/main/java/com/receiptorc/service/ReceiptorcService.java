package com.receiptorc.service;

import com.receiptorc.dto.ReceiptResponseDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReceiptorcService {
    public ReceiptResponseDTO receiptorc(byte[] fileBytes) throws IOException, InterruptedException {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("apikey", List.of("K87885948988957"));
        headers.put("language", List.of("auto"));

        MakeRequestToOdbcFree makeRequestToOdbcFree = new MakeRequestToOdbcFree();
        makeRequestToOdbcFree.makeRequest(headers, fileBytes);
        System.out.println("Log: the application arrived in the service ");
        return null;
    }
}
