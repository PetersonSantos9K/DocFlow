package com.PetersonSantos9K.docflow_api.ingestion;


import com.PetersonSantos9K.docflow_api.ingestion.source.SourceProvider;
import com.PetersonSantos9K.docflow_api.ingestion.source.JGitSourceProvider;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class IngestionService {

    private final SourceProvider sourceProvider;

    public IngestionService(JGitSourceProvider jGit){
        this.sourceProvider = jGit;
    }

    public String repositoryDownloader(String repositoryUrl){

        IngestionContext context = sourceProvider.download(repositoryUrl);

        try (Stream<Path> paths = Files.walk(context.getWorkspacePath())) {
            paths.forEach(System.out::println);

        } catch (IOException err){


        }
        return "";
    }








}
