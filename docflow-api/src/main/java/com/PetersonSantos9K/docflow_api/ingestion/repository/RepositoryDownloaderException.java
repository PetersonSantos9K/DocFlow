package com.PetersonSantos9K.docflow_api.ingestion.repository;

import com.PetersonSantos9K.docflow_api.ingestion.IngestionException;

public class RepositoryDownloaderException extends IngestionException {
    public RepositoryDownloaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryDownloaderException(String message) {
        super(message);
    }


}
