package com.PetersonSantos9K.docflow_api.ingestion.workspace;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

@Component
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

    public static void deleteWorkspace(WorkspaceLocation workspaceLocation){
        try(Stream<Path> paths = Files.walk(workspaceLocation.path())) {

            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new WorkspaceException("Failed to delete workspace: " + workspaceLocation.path(), e);
                        }
                    });

        } catch (IOException e) {
            throw new WorkspaceException("Failed to delete workspace: " + workspaceLocation.path(), e);
        }
    }

}
