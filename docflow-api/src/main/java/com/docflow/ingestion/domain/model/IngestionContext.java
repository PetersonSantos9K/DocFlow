package com.docflow.ingestion.domain.model;

import java.nio.file.Path;
import java.util.UUID;

public record IngestionContext(
         UUID id,
         String owner,
         String repo,
         String subPath,
         String urlRepositoryCloned,
         Path workspacePath){
}
