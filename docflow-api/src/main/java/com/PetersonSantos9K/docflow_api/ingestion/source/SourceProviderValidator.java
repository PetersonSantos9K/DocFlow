package com.PetersonSantos9K.docflow_api.ingestion.source;

import com.PetersonSantos9K.docflow_api.ingestion.domain.exception.SourceException;
import com.PetersonSantos9K.docflow_api.ingestion.domain.model.SourceInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class SourceProviderValidator {

    private static final int MAX_URL_LENGTH = 200;
    private static final List<String> ACCEPTED_PREFIXES = List.of(
            "https://github.com/",
            "https://www.github.com/"
    );
    private static final String CANONICAL_PREFIX = "https://github.com/";

    private static final Pattern OWNER =
            Pattern.compile("[a-zA-Z\\d](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){0,38}");
    private static final Pattern REPO =
            Pattern.compile("[\\w.-]+(?:/[\\w.-]+)*");
    private static final Pattern TRAILING_SLASHES = Pattern.compile("/+$");

    public SourceInfo validateAndExtract(String repositoryUrl) {

        String path = normalize(repositoryUrl);
        String[] parts = path.split("/", 3);

        if (parts.length < 2) {
            throw new SourceException("URL must contain owner and repository");
        }

        String owner = parts[0];
        String name = stripGitSuffix(parts[1]);
        String subPath = parts.length > 2 ? parts[2] : null;

        validateOwner(owner);
        validateRepositoryPath(name + (subPath != null ? "/" + subPath : ""));
        validateName(name);
        return new SourceInfo(owner, name, subPath,  CANONICAL_PREFIX + owner + "/" + name, repositoryUrl);
    }
    private String normalize(String repositoryUrl) {

        if (repositoryUrl == null || repositoryUrl.isBlank()) {
            throw new SourceException("Repository URL must not be empty");
        }

        repositoryUrl = TRAILING_SLASHES.matcher(repositoryUrl.trim()).replaceAll("");
        if (repositoryUrl.length() > MAX_URL_LENGTH) {
            throw new SourceException("Repository URL is too long");
        }
        return removePrefix(repositoryUrl);
    }

    private String removePrefix(String url) {
        String prefix = ACCEPTED_PREFIXES.stream()
                .filter(p -> url.regionMatches(true, 0, p, 0, p.length()))
                .findFirst()
                .orElseThrow(() -> new SourceException("Unsupported URL prefix: " + url));
        return url.substring(prefix.length());
    }

    private String stripGitSuffix(String name) {
        int len = name.length();
        return len > 4 && name.regionMatches(true, len - 4, ".git", 0, 4)
                ? name.substring(0, len - 4)
                : name;
    }

    private void validateOwner(String owner) {
        if (!OWNER.matcher(owner).matches()) {
            throw new SourceException("Invalid owner: " + owner);
        }
    }

    private void validateName(String name){
        if(name.isEmpty() || name.equals(".") || name.equals("..")){
            throw new SourceException("Invalid repository name: " + name);
        }
    }

    private void validateRepositoryPath(String repo) {
        if (!REPO.matcher(repo).matches()) {
            throw new SourceException("Invalid repository name: " + repo);
        }
    }
}
