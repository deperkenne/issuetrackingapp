package de.perceverance.ke.tcho.fo.assignment2.issuetracking.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import de.perceverance.ke.tcho.fo.assignment2.issuetracking.IssueTracker;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.IssueTrackingException;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.ProjectService;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Project;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.view.ConsoleHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class IssueTrackerImpl implements IssueTracker {
	private static final String SCHEMA_PATH = "/schema/project-schema.json"; // Path within resources

	public IssueTrackerImpl() {
		/*
		 * DO NOT REMOVE - REQUIRED FOR GRADING
		 *
		 * YOU CAN EXTEND IT BELOW THIS COMMENT
		 */
	}

	@Override
	public void validate(String path) throws IssueTrackingException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);

			InputStream schemaInputStream = getClass().getResourceAsStream(SCHEMA_PATH);
			if (schemaInputStream == null) {
				throw new IssueTrackingException("Schema file not found: " + SCHEMA_PATH);
			}

			JsonSchema schema = factory.getSchema(schemaInputStream);

			JsonNode json = mapper.readTree(new File(path));
			System.out.println("Loaded JSON for validation: " + json.toString());

			Set<ValidationMessage> validationMessages = schema.validate(json);
			if (!validationMessages.isEmpty()) {
				throw new IssueTrackingException("JSON validation failed: " + validationMessages);
			}
			System.out.println("JSON validation successful.");
		} catch (IOException e) {
			throw new IssueTrackingException("Error reading JSON file: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new IssueTrackingException("Error validating JSON schema: " + e.getMessage(), e);
		}
	}

	@Override
	public ProjectService load(String path) throws IssueTrackingException {
		try {
			System.out.println("Validating JSON file at path: " + path);
			validate(path);  // Validate the JSON file against the schema
			ObjectMapper mapper = new ObjectMapper();
			Project project = mapper.readValue(new File(path), Project.class);
			ProjectServiceImpl projectService = new ProjectServiceImpl();
			projectService.setProject(project);
			System.out.println("Project loaded successfully.");
			return projectService;
		} catch (IOException e) {
			throw new IssueTrackingException("Error loading project from JSON file: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new IssueTrackingException("Unexpected error: " + e.getMessage(), e);
		}
	}

	@Override
	public ProjectService create() {
		return new ProjectServiceImpl();
	}

	// component printToconsole
	public static void printToConsole(String message){
		ConsoleHelper console = ConsoleHelper.build();
		console.getOut().println(message);
	}
}


