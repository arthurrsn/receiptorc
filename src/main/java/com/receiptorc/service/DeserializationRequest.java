package com.receiptorc.service;

import com.receiptorc.exceptions.TechnicalException;
import com.receiptorc.ports.IDeserializationRequest;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class DeserializationRequest implements IDeserializationRequest {
    public <T> T deserialization(String json, ObjectMapper mapper, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (JacksonException e) {
            throw new TechnicalException("Don't be possible made deserialization.", e);
        }
    }
}
