package com.receiptorc.service;

import com.receiptorc.dto.TextOCRSpace;
import com.receiptorc.ports.IDeserializationRequest;
import com.receiptorc.ports.IMakeRequest;
import com.receiptorc.ports.IRequestToOcr;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class RequestToOcr implements IRequestToOcr {
    private final IMakeRequest makeRequest;
    private final IDeserializationRequest deserializationRequest;
    private final Dotenv dotenv = Dotenv.load();

    public RequestToOcr(Request makeRequest, DeserializationRequest deserializationRequest){
        this.makeRequest = makeRequest;
        this.deserializationRequest = deserializationRequest;
    }

    public String requestToOcr(File fileImage, ObjectMapper mapper){
        /*
         * Make a request to ocr space. This will return for us
         * a message in the image.
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
        headers.put("apikey", dotenv.get("API_KEY_OCR"));
        headers.put("language", "auto");

        // Make request and deserialization it
        String resultRequest = makeRequest.makeRequest(builder, url, headers, null);
        TextOCRSpace textOCRSpace = deserializationRequest.deserialization(resultRequest, mapper, TextOCRSpace.class);

        if (textOCRSpace.results() != null) {
            return textOCRSpace.results().getFirst().parsedText();
        } return "0.0";
    }
}
