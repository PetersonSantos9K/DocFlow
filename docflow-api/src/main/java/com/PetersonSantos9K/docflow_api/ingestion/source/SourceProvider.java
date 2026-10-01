package com.PetersonSantos9K.docflow_api.ingestion.source;

import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceInfo;

import java.nio.file.Path;

public interface SourceProvider {
    SourceInfo download(String repositoryUrl, Path workspacePath);
}
