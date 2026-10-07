package com.PetersonSantos9K.docflow_api.ingestion;


import com.PetersonSantos9K.docflow_api.ingestion.service.RepositoryEligibilityService;
import com.PetersonSantos9K.docflow_api.ingestion.source.SourceException;
import com.PetersonSantos9K.docflow_api.ingestion.source.SourceInfo;
import com.PetersonSantos9K.docflow_api.ingestion.source.SourceProvider;
import com.PetersonSantos9K.docflow_api.ingestion.source.JGitSourceProvider;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.TmpWorkspaceProvider;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceException;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceInfo;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceProvider;
import org.springframework.stereotype.Service;

@Service
public class IngestionService {

    private final SourceProvider sourceProvider;
    private final WorkspaceProvider workspaceProvider;
    private final RepositoryEligibilityService repositoryeligibilityService;

    public IngestionService(JGitSourceProvider jGit, TmpWorkspaceProvider tmp, RepositoryEligibilityService repositoryeligibilityService){
        this.sourceProvider = jGit;
        this.workspaceProvider = tmp;
        this.repositoryeligibilityService = repositoryeligibilityService;
    }

    public IngestionContext ingest(String repositoryUrl){

        WorkspaceInfo workspaceInfo = workspaceProvider.createWorkspace();

        try{
            var source = sourceProvider;
            SourceInfo sourceInfo = source.fromRepositoryUrl(repositoryUrl);
            repositoryeligibilityService.isRepoSizeAcceptable(sourceInfo.owner(), sourceInfo.repo());
            source.download(sourceInfo, workspaceInfo.path());

            IngestionContext context = new IngestionContext(
                    workspaceInfo.id(),
                    sourceInfo.repo(),
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
