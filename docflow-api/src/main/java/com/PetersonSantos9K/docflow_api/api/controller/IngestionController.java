package com.PetersonSantos9K.docflow_api.api.controller;

import com.PetersonSantos9K.docflow_api.api.dto.request.IngestionUrlRequestDTO;
import com.PetersonSantos9K.docflow_api.service.ingestion.IngestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class IngestionController {

    private final IngestionService service;


    @PostMapping("/url")
    public String repositoryDownload(@RequestBody IngestionUrlRequestDTO request){
        IO.println(request.url());
        service.repositoryDownloader(request.url());
        return "O teste foi um sucesso!";
    }


}
