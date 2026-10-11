package com.PetersonSantos9K.docflow_api.ingestion.client;

import com.PetersonSantos9K.docflow_api.ingestion.domain.model.RepositoryInfo;

import java.util.Optional;

public interface RepositoryClient {

    Optional<RepositoryInfo> getRepository(String owner, String repo);

}
