package dh.code.dependency.checker.api.client;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dh.code.dependency.checker.domain.Artifact;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.maven.model.Dependency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

//TODO cleanup this class
public class MavenRepositoryClient {
    private static String BASE_URL = "https://search.maven.org/solrsearch/select?q=";
    private final HttpClientBuilder httpClientBuilder;

    public MavenRepositoryClient() {
        httpClientBuilder = HttpClients.custom();
    }

    public void printPomFile(Artifact artifact) {
        System.out.println("https://search.maven.org/remotecontent?filepath=" + artifact.getPomFileName());
    }

    //TODO Clean this the frak up!
    public Artifact findArtifact(Dependency dependency) {
        final String groupId = dependency.getGroupId();
        final String artifactId = dependency.getArtifactId();
        String searchUrl = BASE_URL + "g:" + groupId + "+AND+" + "a:" + artifactId + "&wt=json";
        try {
            CloseableHttpResponse response = httpClientBuilder.build().execute(new HttpGet(toURI(searchUrl)));
            String result = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                    .lines().collect(Collectors.joining("\n"));

            JsonObject object = new Gson().fromJson(result, JsonObject.class);
            int resultsFound = object.get("response").getAsJsonObject().get("numFound").getAsInt();

            //TODO Error handling: missing tags, too many results, no results
            if (resultsFound >= 1) {
                JsonObject doc = object.get("response").getAsJsonObject().get("docs").getAsJsonArray().get(0).getAsJsonObject();
                return Artifact.builder()
                        .currentVersion(dependency.getVersion())
                        .id(doc.get("id").getAsString())
                        .groupId(doc.get("g").getAsString())
                        .artifactId(doc.get("a").getAsString())
                        .latestVersion(doc.get("latestVersion").getAsString())
                        .repositoryId(doc.get("repositoryId").getAsString())
                        .packaging(doc.get("p").getAsString())
                        .timestamp(doc.get("timestamp").getAsLong())
                        .versionCount(doc.get("versionCount").getAsInt())
                        .build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private URI toURI(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            return null;
        }
    }

}
