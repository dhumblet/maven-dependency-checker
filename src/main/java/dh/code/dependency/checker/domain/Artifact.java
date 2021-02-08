package dh.code.dependency.checker.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Builder
@Data
public class Artifact {
    private final String id;
    private final String groupId;
    private final String artifactId;
    private final String currentVersion;
    private final String latestVersion;
    private final String repositoryId;
    private final String packaging;
    private final long timestamp;
    private final int versionCount;

    public boolean hasLatestVersion() {
        return StringUtils.isNoneEmpty(currentVersion, latestVersion) && currentVersion.equals(latestVersion);
    }

    //TODO Om ooit de homepage te tonen, kan geparsed worden uit de pom XML
    public String getPomFileName() {
        return new StringBuilder()
                .append(getGroupId().replace(".", "/"))
                .append("/")
                .append(getArtifactId().replace(".", "/"))
                .append("/")
                .append(getLatestVersion())
                .append("/")
                .append(getArtifactId())
                .append("-")
                .append(getLatestVersion())
                .append(".pom")
                .toString();
    }
}
