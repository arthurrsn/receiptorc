package com.receiptorc.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ParsedResult(
        @JsonAlias("ParsedText") String parsedText
) {}
