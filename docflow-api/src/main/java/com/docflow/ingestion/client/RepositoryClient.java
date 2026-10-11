package com.docflow.ingestion.client;

import com.docflow.ingestion.domain.model.RepositoryInfo;

import java.util.Optional;

public interface RepositoryClient {

    Optional<RepositoryInfo> getRepository(String owner, String repo);

}
