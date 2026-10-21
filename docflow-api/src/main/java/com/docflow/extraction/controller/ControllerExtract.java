package com.docflow.extraction.controller;

import com.docflow.extraction.domain.exception.ExtractionException;
import com.docflow.extraction.domain.model.ControllerInfo;
import com.docflow.extraction.domain.model.EndpointInfo;
import com.docflow.extraction.domain.model.ParameterInfo;
import com.docflow.extraction.domain.model.enums.ParameterLocation;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ControllerExtract {

    private static final Map<String, String> VERBS = Map.of(
            "GetMapping", "GET",
            "PostMapping", "POST",
            "PutMapping", "PUT",
            "PatchMapping", "PATCH",
            "DeleteMapping", "DELETE");

    public ControllerInfo extract(CompilationUnit cu) {
        TypeDeclaration<?> type = cu.getPrimaryType()
                .orElseThrow(() -> new ExtractionException("Arquivo sem tipo principal"));

        String packageName = cu.getPackageDeclaration()
                .map(pd -> pd.getNameAsString())
                .orElse("");

        String basePath = type.getAnnotationByName("RequestMapping")
                .map(this::readPath)
                .orElse("");

        List<EndpointInfo> endpoints = type.getMethods().stream()
                .map(method -> extractEndpoint(method, basePath))
                .flatMap(Optional::stream)
                .toList();

        return new ControllerInfo(type.getNameAsString(), packageName, basePath, endpoints);
    }

    private Optional<EndpointInfo> extractEndpoint(MethodDeclaration method, String basePath) {
        return method.getAnnotations().stream()
                .filter(ann -> VERBS.containsKey(ann.getName().getIdentifier()))
                .findFirst()
                .map(ann -> new EndpointInfo(
                        method.getNameAsString(),
                        VERBS.get(ann.getName().getIdentifier()),
                        joinPaths(basePath, readPath(ann)),
                        extractParameters(method),
                        method.getType().asString(),
                        extractStatus(method),
                        List.of()));
    }

    private String readPath(AnnotationExpr ann) {
        if (ann.isSingleMemberAnnotationExpr()) {
            return ann.asSingleMemberAnnotationExpr().getMemberValue()
                    .asStringLiteralExpr().asString();
        }
        return "";
    }

    private String joinPaths(String base, String path) {
        return ("/" + base + "/" + path).replaceAll("/+", "/")
                .replaceAll("(?<=.)/$", "");
    }

    private List<ParameterInfo> extractParameters(MethodDeclaration method) {
        return method.getParameters().stream()
                .map(this::toParameterInfo)
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<ParameterInfo> toParameterInfo(Parameter p) {
        ParameterLocation location =
                p.isAnnotationPresent("PathVariable")  ? ParameterLocation.PATH
                        : p.isAnnotationPresent("RequestParam")  ? ParameterLocation.QUERY
                        : p.isAnnotationPresent("RequestHeader") ? ParameterLocation.HEADER
                        : p.isAnnotationPresent("RequestBody")   ? ParameterLocation.BODY
                        : null;

        if (location == null) return Optional.empty(); // HttpServletRequest, Principal etc.

        return Optional.of(new ParameterInfo(
                p.getNameAsString(), location, p.getType().asString(), true));
    }

    private Integer extractStatus(MethodDeclaration method) {
        return method.getAnnotationByName("ResponseStatus")
                .filter(AnnotationExpr::isSingleMemberAnnotationExpr)
                .map(a -> a.asSingleMemberAnnotationExpr().getMemberValue())
                .filter(Expression::isFieldAccessExpr)
                .map(e -> HttpStatus.valueOf(e.asFieldAccessExpr().getNameAsString()).value())
                .orElse(null);
    }

}
