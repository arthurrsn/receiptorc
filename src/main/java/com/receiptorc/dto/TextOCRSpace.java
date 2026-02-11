package com.receiptorc.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TextOCRSpace(
        @JsonAlias("ParsedResults") List<ParsedResult> results
) {}

