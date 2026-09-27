package com.PetersonSantos9K.docflow_api.ingestion.workspace;

import java.nio.file.Path;
import java.util.UUID;

public record WorkspaceLocation(
        UUID id,
        Path path
) {
}
