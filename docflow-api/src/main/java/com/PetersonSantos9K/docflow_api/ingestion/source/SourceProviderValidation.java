package com.PetersonSantos9K.docflow_api.ingestion.source;

import com.PetersonSantos9K.docflow_api.ingestion.IngestionContext;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class SourceProviderValidation {

    private static final int MAX_URL_LENGTH = 200;
    private static final List<String> ACCEPTED_PREFIXES = List.of(
            "https://github.com/",
            "https://www.github.com/"
    );

    public IngestionContext validation(String repositoryUrl){

        if(repositoryUrl == null){

        }
        repositoryUrl.trim();
        if(repositoryUrl.isBlank() || repositoryUrl.length() > MAX_URL_LENGTH){

        }


        return null;
    }

    private String formatedUrlHttps(String url){



        return null;
    }

    private String removePrefix(String url){

        String prefix = ACCEPTED_PREFIXES.stream()
                .filter(url::startsWith)
                .findFirst()
                .orElseThrow(null);


        return url.substring(prefix.length());
    }



}
