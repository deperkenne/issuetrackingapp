package de.perceverance.ke.tcho.fo.assignment2.issuetracking.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project {

	@JsonProperty("issues")
	private List<Issue> issues = new LinkedList<>();

	@JsonProperty("milestones")
	private List<Milestone> milestones = new LinkedList<>();

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	public List<Milestone> getMilestones() {
		return milestones;
	}

	public void setMilestones(List<Milestone> milestones) {
		this.milestones = milestones;
	}
}
