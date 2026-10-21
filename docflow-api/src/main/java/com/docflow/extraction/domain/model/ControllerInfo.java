package com.docflow.extraction.domain.model;

import java.util.List;

public record ControllerInfo(
        String name,
        String basePath,
        String javadoc,
        List<EndpointInfo> endpoints
) {
}
