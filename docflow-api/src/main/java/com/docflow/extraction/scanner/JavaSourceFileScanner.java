package com.docflow.extraction.scanner;

import com.docflow.extraction.domain.exception.AnalyzerException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Component
public class JavaSourceFileScanner {

    public List<Path> findJavaFiles(Path rootPath) {
        try (Stream<Path> paths = Files.walk(rootPath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList();
        } catch (IOException err) {
            throw new AnalyzerException(
                    "Failed to scan Java source files.", err
            );
        }
    }

}
