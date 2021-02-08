package dh.code.dependency.checker.model;

import dh.code.dependency.checker.domain.Artifact;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class ReportModel {
    private List<ReportEntryModel> outOfDateEntries = new ArrayList<>();
    private List<ReportEntryModel> upToDateEntries = new ArrayList<>();
    private List<ReportEntryModel> notFoundEntries = new ArrayList<>();

    public ReportModel(Map<String, Artifact> artifacts) {
        artifacts.values().forEach(a -> addEntry(new ReportEntryModel(a)));
    }

    public void addEntry(ReportEntryModel entry) {
        switch (entry.getStatus()) {
            case UP_TO_DATE:
                upToDateEntries.add(entry);
                break;
            case OUT_OF_DATE:
                outOfDateEntries.add(entry);
                break;
            case NOT_FOUND:
            default:
                notFoundEntries.add(entry);
                break;
        }
    }
}
