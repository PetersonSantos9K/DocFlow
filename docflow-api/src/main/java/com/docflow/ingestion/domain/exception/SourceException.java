package com.docflow.ingestion.domain.exception;

public class SourceException extends IngestionException {
    public SourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceException(String message) {
        super(message);
    }
}
