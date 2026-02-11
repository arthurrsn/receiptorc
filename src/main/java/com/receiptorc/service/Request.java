package com.receiptorc.service;

import com.receiptorc.exceptions.TechnicalException;
import com.receiptorc.ports.IMakeRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class Request implements IMakeRequest {

    /**
     * The goal is make a request. This function were thought for abrange more of one situation.
     * This returns string but can receive almost situations.
     * @param builder here's sent the parameter. for example kind a multipart image
     * @param url url link to find address of api
     * @param headers parameters
     * @return a String/json with response about request
     */
    @Override
    public String makeRequest(Optional<MultipartEntityBuilder> builder, String url, Optional<Map<String, String>> headers) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            // Define post request
            HttpPost postRequest = new HttpPost(url);

            // Here we say "Man, if exists header or builder, we add it, but, case no exists, it's all good! Life goes on!"
            headers.ifPresent(stringMap -> stringMap.forEach(postRequest::addHeader));
            builder.ifPresent(MultipartEntityBuilder::build);
            if (builder.isPresent()) {
                HttpEntity multipartEntity = builder.get().build();
                postRequest.setEntity(multipartEntity);
            }

            // execute request and handle response
            try(CloseableHttpResponse response = httpClient.execute(postRequest)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    return EntityUtils.toString(responseEntity);
                }
            }
        } catch (IOException e) {
            throw new TechnicalException("Don't can be execute request: " + e.getMessage());
        }
        // Requests returns nothing
        return null;
    }
}
