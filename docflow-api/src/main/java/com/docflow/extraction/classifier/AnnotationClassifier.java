package com.docflow.extraction.classifier;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AnnotationClassifier {

    public boolean matchesClassAnnotation(
            CompilationUnit compilationUnit,
            String... annotationNames) {

        return compilationUnit.getTypes().stream()
                .anyMatch(type ->
                        Arrays.stream(annotationNames)
                                .anyMatch(type::isAnnotationPresent)
                );
    }

    public boolean matchesMethodAnnotation(
            CompilationUnit compilationUnit,
            String... annotationNames) {

        return compilationUnit.getTypes().stream()
                .flatMap(type -> type.getMembers().stream())
                .filter(MethodDeclaration.class::isInstance)
                .map(MethodDeclaration.class::cast)
                .anyMatch(method ->
                        Arrays.stream(annotationNames)
                                .anyMatch(method::isAnnotationPresent)
                );
    }



}
