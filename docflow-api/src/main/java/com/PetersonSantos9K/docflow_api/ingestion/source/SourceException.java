package com.PetersonSantos9K.docflow_api.ingestion.source;

import com.PetersonSantos9K.docflow_api.ingestion.IngestionException;

public class SourceException extends IngestionException {
    public SourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceException(String message) {
        super(message);
    }
}
