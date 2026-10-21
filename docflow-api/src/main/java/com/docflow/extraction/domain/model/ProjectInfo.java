package com.docflow.extraction.domain.model;

import java.util.List;

public record ProjectInfo <Type>(
        String name,
        String buildTool,
        String javaVersion,
        Type type
) {
}
