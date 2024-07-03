package de.perceverance.ke.tcho.fo.assignment2.issuetracking.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.LinkedList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Issue {

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("severity")
	private Severity severity;

	@JsonProperty("type")
	private Type type;

	@JsonProperty("state")
	private State state;

	@JsonProperty("milestone")
	private Milestone milestone;

	@JsonProperty("dependencies")
	private List<Issue> dependencies = new LinkedList<>();

	public Issue() {
		this.state = State.OPEN; // Default state is OPEN
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Milestone getMilestone() {
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	public List<Issue> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<Issue> dependencies) {
		this.dependencies = dependencies;
	}

	public String toString() {
		String issueNames = "";
		for (Issue dependentIssue : dependencies) {
			issueNames += dependentIssue.getName() + " ";
		}
		String milestone = "";
		if (this.milestone != null) {
			milestone = this.milestone.getName();
		}

		return "Issue [id=" + id + ", name=" + name + ", description="
				+ description + ", severity=" + severity + ", type=" + type
				+ ", state=" + state + ", milestone=" + milestone
				+ ", dependsUponIssues=( " + issueNames + ")]";
	}
}
