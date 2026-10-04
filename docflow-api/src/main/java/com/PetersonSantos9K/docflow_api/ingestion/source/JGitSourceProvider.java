package com.PetersonSantos9K.docflow_api.ingestion.source;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class JGitSourceProvider implements SourceProvider {

    private final SourceProviderValidation sourceProviderValidation;

    public JGitSourceProvider(SourceProviderValidation validation){
        this.sourceProviderValidation = validation;
    }

    @Override
    public SourceInfo fromRepositoryUrl(String repositoryUrl) {
        return sourceProviderValidation.validation(repositoryUrl);

    }

    @Override
    public void download(SourceInfo sourceInfo, Path workspacePath) {

        try(Git git = Git.cloneRepository()
                .setURI(sourceInfo.cloneUrl())
                .setDirectory(workspacePath.toFile())
                .call()){

            if(!Files.isDirectory(workspacePath)) {
                throw new SourceException("Downloaded repository directory does not exists: " + workspacePath);
            }
        } catch (GitAPIException err) {
            throw new SourceException("Failed to download repository: " + sourceInfo.urlRepository(), err);
        } catch (SourceException err) {
            throw err;
        }

    }
}
