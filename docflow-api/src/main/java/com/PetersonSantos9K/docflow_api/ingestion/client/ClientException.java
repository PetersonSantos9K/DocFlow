package com.PetersonSantos9K.docflow_api.ingestion.client;

import com.PetersonSantos9K.docflow_api.ingestion.IngestionException;
public class ClientException extends IngestionException {
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(String message) {
        super(message);
    }
}
