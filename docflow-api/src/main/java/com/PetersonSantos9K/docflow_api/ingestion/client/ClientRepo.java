package com.PetersonSantos9K.docflow_api.ingestion.client;

import java.util.Optional;

public interface ClientRepo {

    Optional<RepositoryInfo> getRepository(String owner, String repo);

}
