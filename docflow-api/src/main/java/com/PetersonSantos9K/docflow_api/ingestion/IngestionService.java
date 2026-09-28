package com.PetersonSantos9K.docflow_api.ingestion;


import com.PetersonSantos9K.docflow_api.ingestion.repository.RepositoryDownloader;
import com.PetersonSantos9K.docflow_api.ingestion.repository.JGitRepositoryDownloader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class IngestionService {

    private final RepositoryDownloader repositoryDownloader;

    public IngestionService(JGitRepositoryDownloader jGit){
        this.repositoryDownloader = jGit;
    }

    public String repositoryDownloader(String repositoryUrl){

        Path path = repositoryDownloader.download(repositoryUrl);

        try (Stream<Path> paths = Files.walk(path)) {
            paths.forEach(System.out::println);

        } catch (IOException err){

        }



        return "";
    }








}
