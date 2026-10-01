package com.PetersonSantos9K.docflow_api.ingestion.source;

import com.PetersonSantos9K.docflow_api.ingestion.IngestionContext;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceLocation;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.LocalWorkspaceProvider;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

@Component
public class JGitSourceProvider implements SourceProvider {

    private final SourceProviderValidation validation;

    public JGitSourceProvider(SourceProviderValidation validation){
        this.validation = validation;
    }

    private IngestionContext downloadRepository(String repositoryUrl){
        IngestionContext context = validation.validation(repositoryUrl);
        WorkspaceLocation workspaceLocation = LocalWorkspaceProvider.createWorkspace();
        context.setId(workspaceLocation.id());
        context.setWorkspacePath(workspaceLocation.path());
        boolean success = false;

        try(Git git = Git.cloneRepository()
                .setURI(context.getUrlRepositoryCloned())
                .setDirectory(workspaceLocation.path().toFile())
                .call()){

            if(!Files.isDirectory(workspaceLocation.path())) {
                throw new SourceException("Downloaded repository directory does not exists: " + workspaceLocation.path());
            }

            success = true;

            return context;
        } catch (GitAPIException err) {
            throw new SourceException("Failed to download repository: " + repositoryUrl, err);
        } catch (SourceException err) {
            throw err;
        } finally {
            if(success == false){
                LocalWorkspaceProvider.deleteWorkspace(workspaceLocation);
            }
        }
    }

    @Override
    public IngestionContext download(String repositoryUrl) {
        return downloadRepository(repositoryUrl);
    }
}
