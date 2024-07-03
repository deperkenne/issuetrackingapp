package de.perceverance.ke.tcho.fo.assignment2.issuetracking;

import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Project;

public interface IssueTracker {

	/**
	 * Validates the JSON file identified by <code>path</code> with a JSON Schema
	 * for {@link Project}
	 * 
	 * @param path
	 *            the path to the JSON file to be validated
	 * @throws IssueTrackingException
	 *             if the file identified by <code>path</code> is not valid
	 */
	void validate(String path) throws IssueTrackingException;

	/**
	 * Loads a JSON file containing a {@link Project} by deserializing it into
	 * memory and validating the content by a JSON Schema
	 * 
	 * @param path
	 *            the path of the JSON file to be deserialized
	 * @return a service handle for manipulating the {@link Project}
	 * @throws IssueTrackingException
	 */
	ProjectService load(String path) throws IssueTrackingException;

	/**
	 * Creates a new and empty Project
	 * 
	 * @return a service handle for manipulating the {@link Project}.
	 */
	ProjectService create();

}
