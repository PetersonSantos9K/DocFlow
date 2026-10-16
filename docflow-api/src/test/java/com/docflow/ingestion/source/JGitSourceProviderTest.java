package com.docflow.ingestion.source;

import com.docflow.ingestion.domain.exception.SourceException;
import com.docflow.ingestion.domain.model.SourceInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JGitSourceProviderTest {

    @Mock
    private SourceProviderValidator sourceProviderValidator;

    @TempDir
    private Path tempDir;

    private JGitSourceProvider sourceProvider;

    @BeforeEach
    void setUp() {
        sourceProvider = new JGitSourceProvider(sourceProviderValidator);
    }
    @Test
    @DisplayName("Should delegate repository URL validation to the validator")
    void shouldDelegateRepositoryUrlValidation() {
        String repositoryUrl = "https://github.com/owner/repository";
        SourceInfo expected = new SourceInfo(
                "owner",
                "repository",
                null,
                repositoryUrl,
                repositoryUrl
        );

        when(sourceProviderValidator.validateAndExtract(repositoryUrl)).thenReturn(expected);
        SourceInfo result = sourceProvider.fromRepositoryUrl(repositoryUrl);

        assertThat(result).isSameAs(expected);
        verify(sourceProviderValidator).validateAndExtract(repositoryUrl);
    }


    @Test
    @DisplayName("Should clone the repository into the workspace")
    void shouldCloneRepositoryIntoWorkspace() throws IOException, GitAPIException {
        Path sourcePath = tempDir.resolve("source");
        Files.createDirectory(sourcePath);
        Path sourceFile = sourcePath.resolve("README.md");
        Files.writeString(sourceFile, "repository content");

        try (Git git = Git.init().setDirectory(sourcePath.toFile()).call()) {
            git.add().addFilepattern("README.md").call();
            git.commit()
                    .setMessage("Initial commit")
                    .setAuthor("Test User", "test@example.com")
                    .call();
        }

        Path workspacePath = tempDir.resolve("workspace");
        SourceInfo sourceInfo = new SourceInfo(
                "owner",
                "repository",
                null,
                sourcePath.toUri().toString(),
                "https://github.com/owner/repository"
        );

        sourceProvider.download(sourceInfo, workspacePath);

        assertThat(Files.isDirectory(workspacePath)).isTrue();
        assertThat(Files.readString(workspacePath.resolve("README.md")))
                .isEqualTo("repository content");
    }

    @Test
    @DisplayName("Should wrap clone failures in a SourceException")
    void shouldWrapCloneFailureInSourceException() {
        Path missingRepositoryPath = tempDir.resolve("missing-repository");
        String repositoryUrl = "https://github.com/owner/repository";
        SourceInfo sourceInfo = new SourceInfo(
                "owner",
                "repository",
                null,
                missingRepositoryPath.toUri().toString(),
                repositoryUrl
        );

        assertThatThrownBy(() -> sourceProvider.download(sourceInfo, tempDir.resolve("workspace")))
                .isInstanceOf(SourceException.class)
                .hasMessage("Failed to download repository: " + repositoryUrl)
                .hasCauseInstanceOf(GitAPIException.class);
    }
}