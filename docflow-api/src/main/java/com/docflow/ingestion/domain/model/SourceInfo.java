package com.docflow.ingestion.domain.model;

public record SourceInfo(
        String owner,
        String repo,
        String subPath,
        String cloneUrl,
        String urlRepository
) {
}
