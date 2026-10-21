package com.docflow.extraction.orchestrator;

import com.docflow.extraction.classifier.JavaClassClassifier;
import com.docflow.extraction.controller.ControllerExtract;
import com.docflow.extraction.domain.model.ControllerInfo;
import com.docflow.extraction.domain.model.ProjectInfoSpringBoot;
import com.docflow.extraction.domain.model.ProjectModel;
import com.docflow.extraction.domain.model.enums.JavaClassType;
import com.docflow.extraction.scanner.JavaSourceAnalyzer;
import com.docflow.extraction.scanner.JavaSourceFileScanner;

import com.github.javaparser.ast.CompilationUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExtractionOrchestrator {

    private final JavaSourceFileScanner sourceFileScanner;
    private final JavaSourceAnalyzer sourceAnalyzer;
    private final JavaClassClassifier classClassifier;

    private final ControllerExtract controllerExtract;


    public ProjectModel<ProjectInfoSpringBoot> extract(Path rootPath){
        List<Path> listPath = sourceFileScanner.findJavaFiles(rootPath);

        List<CompilationUnit> compilationUnits = new ArrayList<>();

        for(Path path : listPath){
            compilationUnits.add(sourceAnalyzer.analyze(path));
        }


        List<CompilationUnit> controllerCompilationsUnits = new ArrayList<>();

        for(CompilationUnit cu : compilationUnits){
            if(classClassifier.classifier(cu) == JavaClassType.CONTROLLER){
                controllerCompilationsUnits.add(cu);
            }
        }

        List<ControllerInfo> controllersInfo = new ArrayList<>();

        for(CompilationUnit cu : controllerCompilationsUnits){
            controllersInfo.add(controllerExtract.extract(cu));
        }



        return new ProjectModel<>(
                null,
                controllersInfo,
                null
        );
    }


}
