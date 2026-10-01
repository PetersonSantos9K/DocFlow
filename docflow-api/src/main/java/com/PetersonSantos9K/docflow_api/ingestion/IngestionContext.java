package com.PetersonSantos9K.docflow_api.ingestion;

import java.nio.file.Path;
import java.util.UUID;

public class IngestionContext{

    private UUID id;
    private String name;
    private String subPath;
    private String urlRepositoryCloned;
    private Path workspacePath;

    public IngestionContext(UUID id, String name, String subPath, String urlRepositoryCloned, Path workspacePath) {
        this.id = id;
        this.name = name;
        this.subPath = subPath;
        this.urlRepositoryCloned = urlRepositoryCloned;
        this.workspacePath = workspacePath;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public String getUrlRepositoryCloned() {
        return urlRepositoryCloned;
    }

    public void setUrlRepositoryCloned(String urlRepositoryCloned) {
        this.urlRepositoryCloned = urlRepositoryCloned;
    }

    public Path getWorkspacePath() {
        return workspacePath;
    }

    public void setWorkspacePath(Path workspacePath) {
        this.workspacePath = workspacePath;
    }

    @Override
    public String toString() {
        return "IngestionContext{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subPath='" + subPath + '\'' +
                ", urlRepositoryCloned='" + urlRepositoryCloned + '\'' +
                ", workspacePath=" + workspacePath +
                '}';
    }
}
