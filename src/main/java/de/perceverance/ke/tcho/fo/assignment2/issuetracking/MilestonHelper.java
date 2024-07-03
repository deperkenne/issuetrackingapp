package de.perceverance.ke.tcho.fo.assignment2.issuetracking;

import java.util.Set;

public class MilestonHelper {
    private  String id ;
    private  String name;

    public Set<String> getIssueId() {
        return IssueId;
    }

    public void setIssueId(Set<String> issueId) {
        IssueId = issueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  Set<String> IssueId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
