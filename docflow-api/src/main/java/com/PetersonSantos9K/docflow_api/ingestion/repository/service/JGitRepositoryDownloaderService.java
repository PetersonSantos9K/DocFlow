package com.PetersonSantos9K.docflow_api.ingestion.repository.service;

import com.PetersonSantos9K.docflow_api.ingestion.repository.RepositoryDownloader;
import com.PetersonSantos9K.docflow_api.ingestion.repository.RepositoryDownloaderException;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.WorkspaceLocation;
import com.PetersonSantos9K.docflow_api.ingestion.workspace.LocalWorkspaceProvider;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class JGitRepositoryDownloaderService implements RepositoryDownloader {

    private Path downloadRepository(String repositoryUrl){
        WorkspaceLocation workspaceLocation = LocalWorkspaceProvider.createWorkspace();


        try(Git git = Git.cloneRepository()
                .setURI(repositoryUrl)
                .setDirectory(workspaceLocation.path().toFile())
                .call()){

            Path path = git.getRepository().getWorkTree().toPath();

            if(!Files.isDirectory(path)){
                throw new RepositoryDownloaderException("Downloaded repository directory does not exists: " + path);
            }
            return path;
        } catch (GitAPIException err){

            throw new RepositoryDownloaderException("Failed to download repository: " + repositoryUrl, err);
        }
    }


    @Override
    public Path download(String repositoryUrl){
        return downloadRepository(repositoryUrl);
    }
}
