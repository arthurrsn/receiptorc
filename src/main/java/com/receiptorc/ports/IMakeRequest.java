package com.receiptorc.ports;

import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.Map;

public interface IMakeRequest {
    String makeRequest(MultipartEntityBuilder builder, String url, Map<String, String> headers, String body);
}
