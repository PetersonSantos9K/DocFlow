package com.docflow.extraction.scanner;

import com.docflow.extraction.domain.exception.AnalyzerException;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class JavaSourceAnalyzer {
    private final JavaParser parser;

    public JavaSourceAnalyzer(){
        var configuration = new ParserConfiguration()
                .setLanguageLevel(
                        ParserConfiguration.LanguageLevel.JAVA_25
                );
        this.parser = new JavaParser(configuration);
    }

    public CompilationUnit analyze(Path filePath) {
        try {
            ParseResult<CompilationUnit> result = parser.parse(filePath);

            if (!result.isSuccessful() || result.getResult().isEmpty()) {
                throw new AnalyzerException(
                        "Failed to parse Java source file: " + filePath
                );
            }
            return result.getResult().get();
        } catch (IOException e) {
            throw new AnalyzerException(
                    "Failed to read Java source file: " + filePath, e
            );
        }
    }
}
