package com.PetersonSantos9K.docflow_api.ingestion.domain.model;

import java.nio.file.Path;
import java.util.UUID;

public record WorkspaceInfo(
        UUID id,
        Path path
) {
}
