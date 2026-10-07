package com.PetersonSantos9K.docflow_api.ingestion.service;

import com.PetersonSantos9K.docflow_api.ingestion.IngestionException;
import com.PetersonSantos9K.docflow_api.ingestion.client.ClientRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.unit.DataSize;

public class RepositoryEligibilityService {

    private final ClientRepo clientRepo;

    private final DataSize maxRepositorySize;

    public RepositoryEligibilityService(@Qualifier("gitHubApiClient") ClientRepo clientRepo, @Value("${integration.max-repository-size}") DataSize maxRepositorySize) {
        this.clientRepo = clientRepo;
        this.maxRepositorySize = maxRepositorySize;
    }

    public void isRepoSizeAcceptable(String owner, String repo) {

        var response = clientRepo.getRepository(owner, repo).orElseThrow(
                () -> new IngestionException("Repository not found or inaccessible: " + owner + "/" + repo)
        );

        long repoSizeInBytes = response.sizeInKb() * 1024L;
        if(repoSizeInBytes > maxRepositorySize.toBytes()){
            throw new IngestionException("Repository size exceeds the maximum allowed limit of " + maxRepositorySize.toMegabytes() + " MB.");
        }
    }
}
