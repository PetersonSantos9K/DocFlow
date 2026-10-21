package com.docflow.extraction.domain.model;

import java.util.Set;

public record ProjectInfoSpringBoot(
        String springBootVersion,
        Set<String> dependencies
) {
}
