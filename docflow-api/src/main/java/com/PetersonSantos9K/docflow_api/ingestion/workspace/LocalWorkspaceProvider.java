package com.PetersonSantos9K.docflow_api.ingestion.workspace;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class LocalWorkspaceProvider {

    public LocalWorkspaceProvider(){}

    public static WorkspaceLocation createWorkspace(){
        UUID id = UUID.randomUUID();
        Path path = Path.of(
                "/tmp/docflow/jobs",
                id.toString()
        );
        try{
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new WorkspaceException("Failed to create workspace: " + path, e);
        }

        return new WorkspaceLocation(id, path);
    }
}
