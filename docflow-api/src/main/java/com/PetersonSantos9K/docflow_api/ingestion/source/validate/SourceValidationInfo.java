package com.PetersonSantos9K.docflow_api.ingestion.source.validate;

public record SourceValidationInfo(
        String name,
        String subPath,
        String cloneUrl
) {
}
