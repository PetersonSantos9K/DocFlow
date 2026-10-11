package com.PetersonSantos9K.docflow_api.ingestion.application;


import com.PetersonSantos9K.docflow_api.ingestion.client.RepositoryClient;
import com.PetersonSantos9K.docflow_api.ingestion.domain.model.IngestionContext;
import com.PetersonSantos9K.docflow_api.ingestion.domain.exception.IngestionException;
import com.PetersonSantos9K.docflow_api.ingestion.domain.model.SourceInfo;
import com.PetersonSantos9K.docflow_api.ingestion.source.SourceProvider;
import com.PetersonSantos9K.docflow_api.ingestion.domain.exception.WorkspaceException;
import com.PetersonSantos9K.docflow_api.ingestion.domain.model.WorkspaceInfo;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;

@Service
public class IngestionOrchestrator {

    private final SourceProvider sourceProvider;
    private final WorkspaceProvider workspaceProvider;
    private final RepositoryClient repositoryClient;
    private final DataSize maxRepositorySize;


    public IngestionOrchestrator(
            @Qualifier("jGitSourceProvider") SourceProvider sourceProvider,
            @Qualifier("tmpWorkspaceProvider") WorkspaceProvider workspaceProvider,
            @Qualifier("gitHubApiClient") RepositoryClient repositoryClient,
            @Value("${integration.max-repository-size}") DataSize maxRepositorySize
            ){
        this.sourceProvider = sourceProvider;
        this.workspaceProvider = workspaceProvider;
        this.repositoryClient = repositoryClient;
        this.maxRepositorySize = maxRepositorySize;
    }

    public IngestionContext ingest(String repositoryUrl){

        SourceInfo sourceInfo = sourceProvider.fromRepositoryUrl(repositoryUrl);
        isRepoSizeAcceptable(sourceInfo.owner(), sourceInfo.repo());
        WorkspaceInfo workspaceInfo = workspaceProvider.createWorkspace();

        try{
            sourceProvider.download(sourceInfo, workspaceInfo.path());
            return new IngestionContext(
                    workspaceInfo.id(),
                    sourceInfo.repo(),
                    sourceInfo.subPath(),
                    sourceInfo.cloneUrl(),
                    workspaceInfo.path()
            );
        } catch (IngestionException err){
            try{
                workspaceProvider.deleteWorkspace(workspaceInfo);
            } catch (WorkspaceException cleanupErr){
                err.addSuppressed(cleanupErr);
            }
            throw err;
        }

    }

    private void isRepoSizeAcceptable(String owner, String repo) {

        var response = repositoryClient.getRepository(owner, repo).orElseThrow(
                () -> new IngestionException("Repository not found or inaccessible: " + owner + "/" + repo)
        );

        long repoSizeInBytes = response.sizeInKb() * 1024L;
        if(repoSizeInBytes > maxRepositorySize.toBytes()){
            throw new IngestionException("Repository size exceeds the maximum allowed limit of " + maxRepositorySize.toMegabytes() + " MB.");
        }
    }


}
