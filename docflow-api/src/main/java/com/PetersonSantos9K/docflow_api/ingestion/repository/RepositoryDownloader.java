package com.PetersonSantos9K.docflow_api.ingestion.repository;

import java.nio.file.Path;

public interface RepositoryDownloader {
    Path download(String repositoryUrl);
}
