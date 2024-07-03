package de.perceverance.ke.tcho.fo.assignment2.issuetracking;

import java.util.List;
import java.util.Set;

import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Issue;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Milestone;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Severity;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Type;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.State;

public interface ProjectService {

	/**
	 * Creates a new {@link Milestone} and adds it to the Project.
	 * 
	 * @param id
	 *            the ID of the new {@link Milestone}. Must not be <code>null</code>
	 *            or empty. Must be a valid ID and unique within the current
	 *            project.
	 * @param name
	 *            the name of the new {@link Milestone}. Must not be
	 *            <code>null</code> or empty.
	 * @param issueIds
	 *            The IDs of the {@link Issue}s due with the new {@link Milestone}. Must not be
	 *            <code>null</code>. May be empty. Must not list the same
	 *            id more than once.
	 * @throws IssueTrackingException
	 *             if the parameters are invalid.
	 */
	void createMilestone(String id, String name, Set<String> issueIds) throws IssueTrackingException;

	/**
	 * Fetches {@link Milestone}s attached to the Project.
	 * 
	 * @return the {@link Milestone}s of the current Project.
	 */
	List<Milestone> getMilestones();

	/**
	 * Deletes a {@link Milestone} by ID. All references to the {@link Milestone},
	 * in {@link Issue}s, etc., are deleted.
	 * 
	 * @param id
	 *            the ID of the {@link Milestone} to be deleted.
	 * @throws IssueTrackingException
	 *             {@link Milestone} with given ID does not exist in project.
	 */
	void removeMilestoneById(String id) throws IssueTrackingException;

	/**
	 * Creates a new {@link Issue}. New {@link Issue}s are in <code>Open</code>
	 * {@link State}.
	 * 
	 * @param id
	 *            the ID of the new {@link Issue}. Must not be <code>null</code> or
	 *            empty. Must be a valid ID and unique within the current project.
	 * @param name
	 *            the name of the new {@link Issue} is a headline to the
	 *            description. Must not be <code>null</code> or empty.
	 * @param description
	 *            a description of the new {@link Issue} shows the problem/feature
	 *            in such Detail, that it can be implemented by those who read it.
	 *            Must not be <code>null</code>, but may be empty.
	 * @param severity
	 *            the severity of the new {@link Issue}.
	 * @param type
	 *            the {@link Type} tells whether to fix a problem or to implement
	 *            something new.
	 * @param milestoneId
	 *            optional relationship that indicates the {@link Issue} is due with
	 *            this {@link Milestone}, represented by its ID. Must not be
	 *            <code>null</code>. If it is empty, no relationship is set. If it
	 *            is non-empty, the ID of the {@link Milestone} needs to exist.
	 * @param dependencies
	 *            optional relationship that indicates the {@link Issue}s that need
	 *            to be closed, so the new {@link Issue} can be closed as well. The
	 *            dependent {@link Issue}s are represented by their IDs. Must not be
	 *            <code>null</code> and the IDs of all {@link Issue}s need to exist
	 *            and must not be <code>null</code> or empty. The dependencies may
	 *            be empty.
	 * @throws IssueTrackingException
	 *             if the parameters are invalid.
	 */
	void createIssue(String id, String name, String description, Severity severity, Type type, String milestoneId,
			Set<String> dependencies) throws IssueTrackingException;

	/**
	 * Fetches a list of the {@link Issue}s attached to the Project.
	 * 
	 * @return the {@link Issue}s of the current Project.
	 */
	List<Issue> getIssues();

	/**
	 * Deletes an {@link Issue} by ID. Also deletes all references to this
	 * {@link Issue} in other {@link Issue}s or {@link Milestone}s.
	 * 
	 * @param id
	 *            the ID of the {@link Issue} to be deleted. Must not be null or
	 *            empty. Must be a valid ID.
	 * @throws IssueTrackingException
	 *             {@link Issue} with given ID does not exist in project or one of
	 *             the dependent {@link Issue}s caused a problem during the deletion
	 *             of the references.
	 */
	void removeIssueById(String id) throws IssueTrackingException;

	/**
	 * Closes an {@link Issue} by ID. An {@link Issue} that depends on other open
	 * {@link Issue}s cannot be closed.
	 * 
	 * @param id
	 *            the ID of the {@link Issue} to be closed. Must not be null or
	 *            empty. Must be a valid ID.
	 * @throws IssueTrackingException
	 *             {@link Issue} with given ID does not exist in project or one of
	 *             the dependent {@link Issue}s is not closed.
	 */
	void closeIssueById(String id) throws IssueTrackingException;

	/**
	 * Prints the current project to the console by serializing it into JSON
	 * 
	 * @throws IssueTrackingException
	 *             if there are errors while serializing the current project.
	 */
	void printJsonToConsole() throws IssueTrackingException;

	/**
	 * Saves the current project to the given file by serializing it into JSON.
	 * Existing files are overridden.
	 * 
	 * @param path
	 *            the path of the file. Must not be <code>null</code> or empty. Must
	 *            be a valid path in the file system.
	 * @throws IssueTrackingException
	 *             if path is <code>null</code> or empty or there are errors during
	 *             serialization.
	 */
	void saveJsonToFile(String path) throws IssueTrackingException;
}
