package com.docflow.extraction.domain.model;

import java.util.List;

public record ProjectModel<Type>(
        ProjectInfo<Type> project,
        List<ControllerInfo> controllers,
        List<SchemaInfo> schemas
) {}