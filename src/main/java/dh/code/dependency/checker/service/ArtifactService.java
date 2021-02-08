package dh.code.dependency.checker.service;

import dh.code.dependency.checker.api.client.MavenRepositoryClient;
import dh.code.dependency.checker.domain.Artifact;
import lombok.NoArgsConstructor;
import org.apache.maven.model.Dependency;

@NoArgsConstructor
public class ArtifactService {
    private MavenRepositoryClient mvnRepositoryClient = new MavenRepositoryClient();

    //TODO Overkill, of net meer in deze service stampen?
    public Artifact buildArtifactFromDependency(Dependency dependency) {
        return mvnRepositoryClient.findArtifact(dependency);
    }
}
