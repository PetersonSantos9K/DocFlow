package com.docflow.ingestion.domain.model;

import java.nio.file.Path;
import java.util.UUID;

public record WorkspaceInfo(
        UUID id,
        Path path
) {
}
