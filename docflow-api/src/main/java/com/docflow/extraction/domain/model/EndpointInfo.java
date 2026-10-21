package com.docflow.extraction.domain.model;

import java.util.List;

public record EndpointInfo(
        String methodName,
        String httpMethod,
        String path,
        List<ParameterInfo> parameters,
        String returnType,
        Integer status,
        List<String> dtos
) {}