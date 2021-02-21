package dh.code.dependency.checker;

import dh.code.dependency.checker.domain.Artifact;
import dh.code.dependency.checker.model.ReportModel;
import dh.code.dependency.checker.service.ArtifactService;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Repository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mojo(name = "dependency-checker", defaultPhase = LifecyclePhase.COMPILE)
public class DependencyCounterMojo extends AbstractMojo {
    List<Dependency> dependencies = new ArrayList<>();
    List<Repository> repositories = new ArrayList<>();
    Map<String, Artifact> artifacts = new HashMap<>();

    ArtifactService artifactService = new ArtifactService();


    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "scope")
    String scope;

    public void execute() throws MojoExecutionException, MojoFailureException {
        dependencies = project.getDependencies();

        artifactService.setRepositories(project.getRepositories());

        for (Object repository : project.getRepositories()) {
            printRepository((Repository) repository);
        }

        printNumberOfDependencies();
        buildArtifactsFromDependencies();
        buildHtmlFromArtifacts();
    }


    private void printRepository(Repository repository) {
        StringBuilder sb = new StringBuilder("Repository:");
        sb.append(repository.getId()).append(" ");
        sb.append(repository.getName()).append(" ");
        sb.append(repository.getUrl()).append(" ");


        getLog().info(sb);
    }

    public void printNumberOfDependencies() {
        long numDependencies = dependencies.stream()
                .filter(d -> (scope == null || scope.isEmpty()) || scope.equals(d.getScope()))
                .count();
        getLog().info("Total dependencies found: " + numDependencies);
    }


    private void buildArtifactsFromDependencies() {
        dependencies.forEach(this::buildArtifactFromDependency);
    }

    private void buildArtifactFromDependency(Dependency dependency) {
        Artifact artifact = artifactService.buildArtifactFromDependency(dependency);
        artifacts.put(artifact.getId(), artifact);
    }

    //TODO Lostrekken uit Mojo
    private void buildHtmlFromArtifacts() {
        ReportModel model = new ReportModel(artifacts);
        try {
            Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            Velocity.init();

            VelocityContext context = new VelocityContext();
            context.put("model", model);

            Writer writer = new FileWriter(new File("report.html"));

            Velocity.mergeTemplate("templates/report.vm", "UTF-8", context, writer);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
