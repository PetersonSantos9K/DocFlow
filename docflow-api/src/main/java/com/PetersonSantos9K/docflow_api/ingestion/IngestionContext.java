package com.PetersonSantos9K.docflow_api.ingestion;

import java.nio.file.Path;
import java.util.UUID;

public class IngestionContext {

    private UUID id;
    private String repositoryUrl;
    private Path workspacePath;

    public IngestionContext(UUID id, String repositoryUrl, Path workspacePath){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public Path getWorkspacePath() {
        return workspacePath;
    }

    public void setWorkspacePath(Path workspacePath) {
        this.workspacePath = workspacePath;
    }
}
