package com.docflow.ingestion.workspace;

import com.docflow.ingestion.domain.exception.WorkspaceException;
import com.docflow.ingestion.domain.model.WorkspaceInfo;
import lombok.extern.slf4j.Slf4j;

public interface WorkspaceProvider {

    WorkspaceInfo createWorkspace() throws WorkspaceException;
    void deleteWorkspace(WorkspaceInfo workspaceInfo) throws WorkspaceException;


}
