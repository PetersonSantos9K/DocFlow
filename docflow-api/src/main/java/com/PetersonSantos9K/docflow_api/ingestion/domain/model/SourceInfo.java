package com.PetersonSantos9K.docflow_api.ingestion.domain.model;

public record SourceInfo(
        String owner,
        String repo,
        String subPath,
        String cloneUrl,
        String urlRepository
) {
}
