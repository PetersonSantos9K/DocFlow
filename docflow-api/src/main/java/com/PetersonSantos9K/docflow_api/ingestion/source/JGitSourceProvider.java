package com.PetersonSantos9K.docflow_api.ingestion.source;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class JGitSourceProvider implements SourceProvider {

    private final SourceProviderValidation validation;

    public JGitSourceProvider(SourceProviderValidation validation){
        this.validation = validation;
    }

    private SourceInfo downloadRepository(String repositoryUrl, Path workspacePath) {
        var sourceValidationInfo = validation.validation(repositoryUrl);

        try(Git git = Git.cloneRepository()
                .setURI(sourceValidationInfo.cloneUrl())
                .setDirectory(workspacePath.toFile())
                .call()){

            if(!Files.isDirectory(workspacePath)) {
                throw new SourceException("Downloaded repository directory does not exists: " + workspacePath);
            }

            return new SourceInfo(
                    sourceValidationInfo.name(),
                    sourceValidationInfo.subPath(),
                    sourceValidationInfo.cloneUrl()
            );
        } catch (GitAPIException err) {

            throw new SourceException("Failed to download repository: " + repositoryUrl, err);
        } catch (SourceException err) {
            throw err;
        }
    }

    @Override
    public SourceInfo download(String repositoryUrl, Path workspacePath) {
        return downloadRepository(repositoryUrl, workspacePath);
    }
}
