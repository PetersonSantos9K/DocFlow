package com.PetersonSantos9K.docflow_api.ingestion.workspace;

public interface WorkspaceProvider {

    WorkspaceInfo createWorkspace() throws WorkspaceException;
    void deleteWorkspace(WorkspaceInfo workspaceInfo) throws WorkspaceException;


}
