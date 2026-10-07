package com.PetersonSantos9K.docflow_api.ingestion.client.github;

import com.PetersonSantos9K.docflow_api.ingestion.client.ClientRepo;
import com.PetersonSantos9K.docflow_api.ingestion.client.RepositoryInfo;
import com.PetersonSantos9K.docflow_api.ingestion.client.ClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Component
public class GitHubApiClient implements ClientRepo {

    private final RestClient restClient;

    public GitHubApiClient(@Qualifier("gitHubRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Optional<RepositoryInfo> getRepository(String owner, String repo) {

        try{
            return Optional.ofNullable(
                    restClient.get()
                            .uri("/repos/{owner}/{repo}", owner, repo)
                            .retrieve()
                            .body(RepositoryInfo.class)
            );
        } catch (HttpClientErrorException.NotFound e){
            return Optional.empty();
        } catch (HttpClientErrorException.Unauthorized e){
            throw new ClientException("Unauthorized access to GitHub API. Please check your credentials.", e);
        } catch (HttpClientErrorException.Forbidden e){
            throw new ClientException("Access to GitHub API is forbidden.", e);
        } catch (RestClientException e){
            throw new ClientException("An error occurred while accessing the GitHub API.", e);
        }
    }
}
