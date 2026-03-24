package com.receiptorc.ports;

import tools.jackson.databind.ObjectMapper;

public interface IRequestToAi {
    String requestToAI(String messageOcr, ObjectMapper mapper);
}
