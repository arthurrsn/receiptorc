package com.receiptorc.service;

import com.receiptorc.dto.TextOCRSpace;
import com.receiptorc.exceptions.TechnicalException;
import com.receiptorc.ports.IDeserializationRequest;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class DeserializationRequest implements IDeserializationRequest {
    public String deserialization(String json){
        ObjectMapper mapper = new ObjectMapper();
        TextOCRSpace textMapper = null;

        try {
            textMapper = mapper.readValue(json, TextOCRSpace.class);
        } catch (JacksonException e) {
            throw new TechnicalException("Don't be possible made desserialization.", e);
        }

        return textMapper.results().getFirst().parsedText();

    }
}
