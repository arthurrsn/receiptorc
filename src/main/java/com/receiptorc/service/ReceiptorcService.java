package com.receiptorc.service;

import com.receiptorc.dto.ReceiptResponseDTO;
import com.receiptorc.ports.IDeserializationRequest;
import com.receiptorc.ports.IMakeRequest;
import com.receiptorc.ports.IReceiptService;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ReceiptorcService implements IReceiptService {
    private final IMakeRequest makeRequest;
    private final IDeserializationRequest deserializationRequest;

    public ReceiptorcService(Request makeRequest, DeserializationRequest deserializationRequest){
        this.makeRequest = makeRequest;
        this.deserializationRequest = deserializationRequest;
    }

    /**
     * Orchestra all situations about image ocr
     * @param fileImage receives a receipt image to make ocr
     */
    public ReceiptResponseDTO receiptorc(File fileImage) {
        /*
         * Make a request to ocr space. This will return for us
         * a mensage in the image.
         */

        // Sent File image as a Multipart
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody(
                "file",
                fileImage
        );

        // Defining request scope with url and headers
        String url = "https://api.ocr.space/parse/image";
        Map<String, String> headers = new HashMap<>();
        headers.put("apikey", "K87885948988957");
        headers.put("language", "auto");

        // Make request and deserialization it
        String resultRequest = makeRequest.makeRequest(Optional.of(builder), url, Optional.of(headers));
        String result = deserializationRequest.deserialization(resultRequest);

        /*
         * Here we will to make request for AI. Our goal is extract only price of receipt
         * We will use a free chatbot, let's use open router
         */
        return null;
    }
}
