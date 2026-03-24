package com.receiptorc.service;

import com.receiptorc.dto.ChatResponse;
import com.receiptorc.exceptions.TechnicalException;
import com.receiptorc.ports.IDeserializationRequest;
import com.receiptorc.ports.IMakeRequest;
import com.receiptorc.ports.IRequestToAi;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class RequestToAi implements IRequestToAi {
    private final IMakeRequest makeRequest;
    private final IDeserializationRequest deserializationRequest;
    private final Dotenv dotenv = Dotenv.load();

    public RequestToAi(Request makeRequest, DeserializationRequest deserializationRequest){
        this.makeRequest = makeRequest;
        this.deserializationRequest = deserializationRequest;
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
        if (result.contains("{\"error\":")){
            throw new TechnicalException("The application has an error to request AI");
        }

        ChatResponse chatResponse = deserializationRequest.deserialization(result, mapper, ChatResponse.class);
        if (chatResponse.choices() != null) {
            return chatResponse.choices().getFirst().message().content();
        } return "0.0";
    }
}
