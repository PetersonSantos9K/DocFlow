package com.docflow.ingestion.source;

import com.docflow.ingestion.domain.exception.SourceException;
import com.docflow.ingestion.domain.model.SourceInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component("jGitSourceProvider")
public class JGitSourceProvider implements SourceProvider {

    private final SourceProviderValidator sourceProviderValidator;

    public JGitSourceProvider(SourceProviderValidator validation){
        this.sourceProviderValidator = validation;
    }

    @Override
    public SourceInfo fromRepositoryUrl(String repositoryUrl) {
        return sourceProviderValidator.validateAndExtract(repositoryUrl);

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
