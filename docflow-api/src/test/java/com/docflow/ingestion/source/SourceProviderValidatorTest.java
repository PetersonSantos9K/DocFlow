package com.docflow.ingestion.source;

import com.docflow.ingestion.domain.exception.SourceException;
import com.docflow.ingestion.domain.model.SourceInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class SourceProviderValidatorTest {

    private SourceProviderValidator sourceProviderValidator;

    @BeforeEach
    void setup(){
        sourceProviderValidator = new SourceProviderValidator();
    }

    @Test
    @DisplayName("Should accept a valid repository URL")
    void shouldAcceptValidUrl() {
        String prefix = "https://github.com";
        String owner = "PetersonSantos9K";
        String repo = "repository-teste-docflow";
        String url = prefix + "/" + owner + "/" + repo;

        var obj = sourceProviderValidator.validateAndExtract(url);
        assertThat(obj)
                .isNotNull()
                .isInstanceOf(SourceInfo.class);

        assertThat(obj.owner()).isEqualTo(owner);
        assertThat(obj.repo()).isEqualTo(repo);
        assertThat(obj.subPath()).isNullOrEmpty();
        assertThat(obj.cloneUrl()).isEqualTo(url);
        assertThat(obj.urlRepository()).isEqualTo(url);
    }

    @Test
    @DisplayName("Should accept a repository URL with the www prefix")
    void shouldAcceptUrlWithWwwPrefix() {
        String owner = "PetersonSantos9K";
        String repo = "repository-teste-docflow";
        String url = "https://www.github.com/" + owner + "/" + repo;

        var obj = sourceProviderValidator.validateAndExtract(url);

        assertThat(obj.owner()).isEqualTo(owner);
        assertThat(obj.repo()).isEqualTo(repo);
        assertThat(obj.cloneUrl())
                .isEqualTo("https://github.com/" + owner + "/" + repo);
        assertThat(obj.urlRepository()).isEqualTo(url);
    }

    @Test
    @DisplayName("Should extract the repository subpath")
    void shouldExtractRepositorySubpath() {
        String owner = "PetersonSantos9K";
        String repo = "repository-teste-docflow";
        String subPath = "src/main/java";
        String url = "https://github.com/" + owner + "/" + repo + "/" + subPath;

        var obj = sourceProviderValidator.validateAndExtract(url);

        assertThat(obj.owner()).isEqualTo(owner);
        assertThat(obj.repo()).isEqualTo(repo);
        assertThat(obj.subPath()).isEqualTo(subPath);
        assertThat(obj.cloneUrl())
                .isEqualTo("https://github.com/" + owner + "/" + repo);
        assertThat(obj.urlRepository()).isEqualTo(url);
    }

    @Test
    @DisplayName("Should reject a null repository URL")
    void shouldRejectNullRepositoryUrl() {
        String url = null;

        assertThatThrownBy(() ->
                sourceProviderValidator.validateAndExtract(url)
        )
                .isInstanceOf(SourceException.class)
                .hasMessage("Repository URL must not be empty");
    }

    @Test
    @DisplayName("Should reject a URL without owner and repository")
    void shouldRejectUrlWithoutOwnerAndRepository() {
        String url = "https://github.com/";

        assertThatThrownBy(() ->
                sourceProviderValidator.validateAndExtract(url)
        )
                .isInstanceOf(SourceException.class)
                .hasMessage("URL must contain owner and repository");
    }

    @Test
    @DisplayName("Should reject a blank repository URL")
    void shouldRejectBlankRepositoryUrl() {
        String url = "   ";

        assertThatThrownBy(() ->
                sourceProviderValidator.validateAndExtract(url)
        )
                .isInstanceOf(SourceException.class)
                .hasMessage("Repository URL must not be empty");
    }


    @Test
    @DisplayName("Should reject a repository URL exceeding the maximum length")
    void shouldRejectUrlExceedingMaxLength(){
        String url = "https://github.com/PetersonSantos9K/" + "a".repeat(165);
        assertThat(url).hasSize(201);
        assertThatThrownBy(() ->
            sourceProviderValidator.validateAndExtract(url)
        )
                .isInstanceOf(SourceException.class)
                .hasMessage("Repository URL is too long");
    }

    @Test
    @DisplayName("Should reject a repository URL with an unsupported prefix")
    void shouldRejectUrlWithUnsupportedPrefix() {
        String url = "https://gitlab.com/PetersonSantos9K/repository-teste-docflow";

        assertThatThrownBy(() ->
                sourceProviderValidator.validateAndExtract(url)
        )
                .isInstanceOf(SourceException.class)
                .hasMessage("Unsupported repository URL prefix");
    }

    @Test
    @DisplayName("Should reject a repository URL with an invalid owner")
    void shouldRejectUrlWithInvalidOwner() {
        String url = "https://github.com/Peterson--Santos/repository-teste-docflow";

        assertThatThrownBy(() ->
                sourceProviderValidator.validateAndExtract(url)
        )
                .isInstanceOf(SourceException.class)
                .hasMessage("Invalid repository owner");
    }

    @Test
    @DisplayName("Should reject a repository URL with an invalid repository path")
    void shouldRejectUrlWithInvalidPath() {
        String url = "https://github.com/PetersonSantos9K/repository teste";

        assertThatThrownBy(() ->
                sourceProviderValidator.validateAndExtract(url)
        )
                .isInstanceOf(SourceException.class)
                .hasMessage("Invalid repository path");
    }

    @Test
    @DisplayName("Should reject a repository name equal to dot")
    void shouldRejectRepositoryNameEqualToDot() {
        String url = "https://github.com/PetersonSantos9K/.";

        assertThatThrownBy(() ->
                sourceProviderValidator.validateAndExtract(url)
        )
                .isInstanceOf(SourceException.class)
                .hasMessage("Invalid repository name");
    }


}