package com.docflow.ingestion.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RepositoryInfo(
        @JsonProperty("private") boolean isPrivate,
        @JsonProperty("size") long sizeInKb,
        String language) {

}
