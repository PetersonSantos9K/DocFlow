package com.docflow.extraction.classifier;

import com.docflow.extraction.domain.model.EndpointInfo;
import com.docflow.extraction.domain.model.enums.JavaClassType;
import com.github.javaparser.ast.CompilationUnit;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JavaClassClassifier {

    private final AnnotationClassifier annotationClassifier;
    private static final String[] CONTROLLER_ANNOTATIONS = {"RestController", "Controller"};
    public JavaClassType classifier(CompilationUnit compilationUnit){
        if(annotationClassifier.matchesClassAnnotation(
                compilationUnit, CONTROLLER_ANNOTATIONS )){
            return JavaClassType.CONTROLLER;
        }

        return JavaClassType.UNKNOWN;
    }




}
