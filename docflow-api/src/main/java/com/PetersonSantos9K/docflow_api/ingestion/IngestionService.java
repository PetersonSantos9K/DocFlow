package com.PetersonSantos9K.docflow_api.ingestion;


import com.PetersonSantos9K.docflow_api.ingestion.source.SourceException;
import com.PetersonSantos9K.docflow_api.ingestion.source.SourceInfo;
import com.PetersonSantos9K.docflow_api.ingestion.source.SourceProvider;
import com.PetersonSantos9K.docflow_api.ingestion.source.JGitSourceProvider;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.TmpWorkspaceProvider;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceException;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceInfo;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceProvider;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class IngestionService {

    private final SourceProvider sourceProvider;
    private final WorkspaceProvider workspaceProvider;
    public IngestionService(JGitSourceProvider jGit, TmpWorkspaceProvider tmp){
        this.sourceProvider = jGit;
        this.workspaceProvider = tmp;
    }

    public IngestionContext ingest(String repositoryUrl){

        WorkspaceInfo workspaceInfo = workspaceProvider.createWorkspace();

        try{
            var source = sourceProvider;
            SourceInfo sourceInfo = source.fromRepositoryUrl(repositoryUrl);
            source.download(sourceInfo, workspaceInfo.path());

            IngestionContext context = new IngestionContext(
                    workspaceInfo.id(),
                    sourceInfo.name(),
                    sourceInfo.subPath(),
                    sourceInfo.cloneUrl(),
                    workspaceInfo.path()
            );

            return context;
        } catch (SourceException err){
            try{
                workspaceProvider.deleteWorkspace(workspaceInfo);
            } catch (WorkspaceException cleanupErr){
                err.addSuppressed(cleanupErr);
            }
            throw err;
        }

    }
}
