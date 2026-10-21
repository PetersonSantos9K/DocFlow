package com.docflow.extraction.domain.model;

import com.docflow.extraction.domain.model.enums.ParameterLocation;

public record ParameterInfo(
        String name,
        ParameterLocation location,
        String type,
        boolean required
) {
}
