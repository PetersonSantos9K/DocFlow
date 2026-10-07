package com.PetersonSantos9K.docflow_api.ingestion.source;

public record SourceInfo(
        String owner,
        String repo,
        String subPath,
        String cloneUrl,
        String urlRepository
) {
}
