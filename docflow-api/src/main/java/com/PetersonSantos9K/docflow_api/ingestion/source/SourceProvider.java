package com.PetersonSantos9K.docflow_api.ingestion.source;

import com.PetersonSantos9K.docflow_api.ingestion.IngestionContext;

public interface SourceProvider {
    IngestionContext download(String repositoryUrl);
}
