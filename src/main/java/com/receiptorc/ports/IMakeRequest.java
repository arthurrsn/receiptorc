package com.receiptorc.ports;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.Map;
import java.util.Optional;

public interface IMakeRequest {
    String makeRequest(Optional<MultipartEntityBuilder> builder, String url, Optional<Map<String, String>> headers) throws RuntimeException;
}
