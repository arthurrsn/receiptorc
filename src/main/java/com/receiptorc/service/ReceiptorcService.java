package com.receiptorc.service;

import com.receiptorc.dto.ChatResponse;
import com.receiptorc.dto.ReceiptResponseDTO;
import com.receiptorc.dto.TextOCRSpace;
import com.receiptorc.ports.IDeserializationRequest;
import com.receiptorc.ports.IMakeRequest;
import com.receiptorc.ports.IReceiptService;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReceiptorcService implements IReceiptService {
    private final IMakeRequest makeRequest;
    private final IDeserializationRequest deserializationRequest;
    private final Dotenv dotenv = Dotenv.load();

    public ReceiptorcService(Request makeRequest, DeserializationRequest deserializationRequest){
        this.makeRequest = makeRequest;
        this.deserializationRequest = deserializationRequest;
    }

    /**
     * Orchestra all situations about image ocr
     * @param fileImage receives a receipt image to make ocr
     */
    public ReceiptResponseDTO receiptorc(File fileImage) {
        ObjectMapper mapper = new ObjectMapper();

        String messageOcr = this.requestToOcr(fileImage, mapper);
        String valueReceiptByAI = this.requestToAI(messageOcr, mapper);

        return new ReceiptResponseDTO(valueReceiptByAI);
    }

    public String requestToAI(String messageOcr, ObjectMapper mapper){
        /*
         * Here we will to make request for AI. Our goal is extract only price of receipt
         * We will use a free chatbot, let's use open router
         */
        String url = "https://openrouter.ai/api/v1/chat/completions";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer %s".formatted(dotenv.get("API_KEY_AI")));

        String fullPrompt = """
                You are an expert in extracting receipt data via OCR. Your task is to identify the total amount paid in the receipt text provided.
                
                CRITICAL RULES:
                1. Respond ONLY with the number in decimal format (use a period '.' as the separator).
                2. Do NOT include currency symbols, text, explanations, or labels.
                3. If multiple values exist, identify the 'Total' or 'Amount Due'.
                4. If the value cannot be found, respond with '0.0'.
                
                Example Input: 'TOTAL R$ 150,50'
                Example Output: 150.50
                
                OCR Text to process: %s
                """.formatted(messageOcr);

        String jsonBody = """
                {
                    "model": "openrouter/free",
                    "messages": [
                        {
                            "role": "user",
                            "content": "%s"
                        }
                    ]
                }
                """.formatted(fullPrompt.replace("\"", "\\\""));
        String result = makeRequest.makeRequest(null, url, headers, jsonBody);
        ChatResponse chatResponse = deserializationRequest.deserialization(result, mapper, ChatResponse.class);
        return chatResponse.choices().getFirst().message().content();
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
        return textOCRSpace.results().getFirst().parsedText();
    }

}
