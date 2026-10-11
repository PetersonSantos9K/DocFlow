package com.docflow.ingestion.domain.exception;

public class IngestionException extends RuntimeException {
    public IngestionException(String message, Throwable cause) {
        super(message, cause);
    }
    public IngestionException(String message) {
        super(message);
    }

}
