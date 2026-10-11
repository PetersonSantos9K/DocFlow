package com.docflow.ingestion.source;

import com.docflow.ingestion.domain.model.SourceInfo;

import java.nio.file.Path;

public interface SourceProvider {

    SourceInfo fromRepositoryUrl(String repositoryUrl);
    void download(SourceInfo sourceInfo, Path workspacePath);

}
