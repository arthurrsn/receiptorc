package com.receiptorc.ports;

import tools.jackson.databind.ObjectMapper;

public interface IDeserializationRequest {
    <T> T deserialization(String json, ObjectMapper mapper, Class<T> tClass);
}