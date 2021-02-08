package dh.code.dependency.checker.model;

import dh.code.dependency.checker.domain.Artifact;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import static dh.code.dependency.checker.model.ReportEntryModel.Status.*;

@Data
public class ReportEntryModel {
    private final String groupId;
    private final String artifactId;
    private final String currentVersion;
    private final String lastVersion;
    private Status status;

    public ReportEntryModel(Artifact artifact) {
        groupId = artifact.getGroupId();
        artifactId = artifact.getArtifactId();
        currentVersion = artifact.getCurrentVersion();
        lastVersion = artifact.getLatestVersion();
        calculateStatus();
    }

    private void calculateStatus() {
        if (StringUtils.isNoneEmpty(getCurrentVersion(), getLastVersion())) {
            if (getCurrentVersion().equals(getLastVersion())) {
                setStatus(UP_TO_DATE);
            } else {
                setStatus(OUT_OF_DATE);
            }
        } else {
            setStatus(NOT_FOUND);
        }
    }

    enum Status {
        UP_TO_DATE,
        OUT_OF_DATE,
        NOT_FOUND;
    }
}
