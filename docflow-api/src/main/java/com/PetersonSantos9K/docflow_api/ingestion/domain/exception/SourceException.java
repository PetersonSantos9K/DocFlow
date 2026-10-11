package com.PetersonSantos9K.docflow_api.ingestion.domain.exception;

public class SourceException extends IngestionException {
    public SourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceException(String message) {
        super(message);
    }
}
