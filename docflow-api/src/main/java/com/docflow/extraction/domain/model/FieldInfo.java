package com.docflow.extraction.domain.model;

import java.util.List;

public record FieldInfo(
        String name,
        String type,
        List<String> validations
) {
}
