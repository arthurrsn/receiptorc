package com.receiptorc.ports;

import tools.jackson.databind.ObjectMapper;

import java.io.File;

public interface IRequestToOcr {
    String requestToOcr(File fileImage, ObjectMapper mapper);
}
