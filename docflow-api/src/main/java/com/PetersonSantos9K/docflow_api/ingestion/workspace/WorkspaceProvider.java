package com.PetersonSantos9K.docflow_api.ingestion.workspace;

import com.PetersonSantos9K.docflow_api.ingestion.domain.exception.WorkspaceException;
import com.PetersonSantos9K.docflow_api.ingestion.domain.model.WorkspaceInfo;

public interface WorkspaceProvider {

    WorkspaceInfo createWorkspace() throws WorkspaceException;
    void deleteWorkspace(WorkspaceInfo workspaceInfo) throws WorkspaceException;


}
