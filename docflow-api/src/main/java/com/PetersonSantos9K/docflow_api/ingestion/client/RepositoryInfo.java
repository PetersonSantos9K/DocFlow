package com.PetersonSantos9K.docflow_api.ingestion.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RepositoryInfo(
        @JsonProperty("private") boolean isPrivate,
        @JsonProperty("size") long sizeInKb,
        String language) {

}
