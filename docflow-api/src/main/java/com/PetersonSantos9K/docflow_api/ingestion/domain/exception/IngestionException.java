package com.PetersonSantos9K.docflow_api.ingestion.domain.exception;

public class IngestionException extends RuntimeException {
    public IngestionException(String message, Throwable cause) {
        super(message, cause);
    }
    public IngestionException(String message) {
        super(message);
    }

}
