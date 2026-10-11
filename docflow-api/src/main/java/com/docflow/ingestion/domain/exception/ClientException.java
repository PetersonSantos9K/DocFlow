package com.docflow.ingestion.domain.exception;

public class ClientException extends IngestionException {
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(String message) {
        super(message);
    }
}
