package com.docflow.extraction.domain.model;

import java.util.Set;

public record SchemaInfo(
        String name,
        String kind,
        Set<FieldInfo> fields
) {
}
