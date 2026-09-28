package com.PetersonSantos9K.docflow_api.ingestion.source;

import com.PetersonSantos9K.docflow_api.ingestion.IngestionContext;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceLocation;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.LocalWorkspaceProvider;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class JGitSourceProvider implements SourceProvider {

    private IngestionContext jGitDownloadRepository(String repositoryUrl){
        WorkspaceLocation workspaceLocation = LocalWorkspaceProvider.createWorkspace();

        try(Git git = Git.cloneRepository()
                .setURI(repositoryUrl)
                .setDirectory(workspaceLocation.path().toFile())
                .call()){

            Path path = git.getRepository().getWorkTree().toPath();

            if(!Files.isDirectory(path)){
                throw new SourceException("Downloaded repository directory does not exists: " + path);
            }
            return new IngestionContext(workspaceLocation.id(), repositoryUrl, path);
        } catch (GitAPIException err){

            throw new SourceException("Failed to download repository: " + repositoryUrl, err);
        }
    }

    @Override
    public IngestionContext download(String repositoryUrl) {
        return jGitDownloadRepository(repositoryUrl);
    }
}
