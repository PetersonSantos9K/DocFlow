package com.PetersonSantos9K.docflow_api.ingestion;


import com.PetersonSantos9K.docflow_api.ingestion.service.RepositoryEligibilityService;
import com.PetersonSantos9K.docflow_api.ingestion.source.SourceException;
import com.PetersonSantos9K.docflow_api.ingestion.source.SourceInfo;
import com.PetersonSantos9K.docflow_api.ingestion.source.SourceProvider;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceException;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceInfo;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class IngestionService {

    private final SourceProvider sourceProvider;
    private final WorkspaceProvider workspaceProvider;
    private final RepositoryEligibilityService repositoryEligibilityService;

    public IngestionService(@Qualifier("jGitSourceProvider") SourceProvider sourceProvider, @Qualifier("tmpWorkspaceProvider") WorkspaceProvider workspaceProvider, RepositoryEligibilityService repositoryEligibilityService){
        this.sourceProvider = sourceProvider;
        this.workspaceProvider = workspaceProvider;
        this.repositoryEligibilityService = repositoryEligibilityService;
    }

    public IngestionContext ingest(String repositoryUrl){

        SourceInfo sourceInfo = sourceProvider.fromRepositoryUrl(repositoryUrl);
        repositoryEligibilityService.isRepoSizeAcceptable(sourceInfo.owner(), sourceInfo.repo());
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
}
