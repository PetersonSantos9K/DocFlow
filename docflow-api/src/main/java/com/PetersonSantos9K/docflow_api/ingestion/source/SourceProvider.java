package com.PetersonSantos9K.docflow_api.ingestion.source;

import com.PetersonSantos9K.docflow_api.ingestion.domain.model.SourceInfo;

import java.nio.file.Path;

public interface SourceProvider {

    SourceInfo fromRepositoryUrl(String repositoryUrl);
    void download(SourceInfo sourceInfo, Path workspacePath);

}
