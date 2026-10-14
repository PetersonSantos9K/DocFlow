package com.docflow.ingestion.orchestrator;

import com.docflow.ingestion.client.RepositoryClient;
import com.docflow.ingestion.domain.model.IngestionContext;
import com.docflow.ingestion.domain.model.RepositoryInfo;
import com.docflow.ingestion.domain.model.SourceInfo;
import com.docflow.ingestion.domain.model.WorkspaceInfo;
import com.docflow.ingestion.source.SourceProvider;
import com.docflow.ingestion.workspace.WorkspaceProvider;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.unit.DataSize;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngestionOrchestratorTest {

    @Mock
    private SourceProvider sourceProvider;
    @Mock
    private WorkspaceProvider workspaceProvider;
    @Mock
    private RepositoryClient repositoryClient;
    @TempDir
    Path tempDir;

    private IngestionOrchestrator orchestrator;

    @BeforeEach
    void setUp(){
        orchestrator = new IngestionOrchestrator(
                sourceProvider,
                workspaceProvider,
                repositoryClient,
                DataSize.ofMegabytes(30)
        );
    }


    @Test
    @DisplayName("Should return an ingestion context when ingestion succeeds")
    void shouldReturnIngestionContextWhenIngestionSucceeds() {

        String url = "https://github.com/PetersonSantos9K/repository-teste-docflow";
        SourceInfo sourceInfo = new SourceInfo("PetersonSantos9K", "repository-teste-docflow", null, "https://github.com/PetersonSantos9K/repository-teste-docflow", url);
        when(sourceProvider.fromRepositoryUrl(url)).thenReturn(sourceInfo);

        Path workspacePath = tempDir.resolve("workspace");
        WorkspaceInfo workspaceInfo = new WorkspaceInfo(UUID.randomUUID(), workspacePath);
        when(workspaceProvider.createWorkspace()).thenReturn(workspaceInfo);
        Optional<RepositoryInfo> repositoryInfo = Optional.of(new RepositoryInfo(false, 30, "Java"));
        when(repositoryClient.getRepository(sourceInfo.owner(), sourceInfo.repo())).thenReturn(repositoryInfo);
        IngestionContext context = orchestrator.ingest(url);
        verify(sourceProvider).download(sourceInfo, workspaceInfo.path());
        assertThat(context)
                .isNotNull();
        assertThat(context.id())
                .isEqualTo(workspaceInfo.id());
        assertThat(context.owner())
                .isEqualTo(sourceInfo.owner());
        assertThat(context.repo())
                .isEqualTo(sourceInfo.repo());
        assertThat(context.subPath())
                .isEqualTo(sourceInfo.subPath());
        assertThat(context.workspacePath())
                .isEqualTo(workspaceInfo.path());
        assertThat(context.urlRepositoryCloned())
                .isEqualTo(sourceInfo.urlRepository());

    }
}