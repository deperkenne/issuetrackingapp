package de.perceverance.ke.tcho.fo.assignment2.issuetracking;

import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Severity;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.State;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Type;

import java.util.Set;

public class IssueHelper {
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private String Id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  String name;
    private  String description;

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Severity severity;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    private State state;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    public Set<String> getIssueList() {
        return issueList;
    }


    private Type type;

    public void setIssueList(Set<String> issueList) {
        this.issueList = issueList;
    }

    private Set<String> issueList;

    public String getMileston() {
        return mileston;
    }

    public void setMileston(String mileston) {
        this.mileston = mileston;
    }

    private String mileston;
}
