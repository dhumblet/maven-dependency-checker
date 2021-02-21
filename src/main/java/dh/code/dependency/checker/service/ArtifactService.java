package dh.code.dependency.checker.service;

import dh.code.dependency.checker.api.client.MavenRepositoryClient;
import dh.code.dependency.checker.domain.Artifact;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Repository;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
public class ArtifactService {
    private final MavenRepositoryClient mvnRepositoryClient = new MavenRepositoryClient();
    private List<Repository> repositories = new ArrayList<>();


    //TODO Overkill, of net meer in deze service stampen?
    public Artifact buildArtifactFromDependency(Dependency dependency) {
        try {
            return mvnRepositoryClient.findArtifact(dependency);
        } catch (Exception e) {
            System.err.println("Error building artifact from dependency: " + dependency);
            return null;
        }
    }
}
