package de.perceverance.ke.tcho.fo.assignment2.issuetracking.validationInput;

import de.perceverance.ke.tcho.fo.assignment2.issuetracking.IssueTrackingException;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.logic.IssueTrackerImpl;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.logic.ValidationHelper;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Issue;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Severity;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.Type;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Set;

public class ValidatePath {

        private  String error;


        public static boolean pathToValid(String path) throws IssueTrackingException {
            ValidatePath validatePath = new ValidatePath();
            try {
                if (!Files.exists(Path.of(path))) {
                    validatePath.setError("This path does not exist");
                    throw new IssueTrackingException(validatePath.getError());
                } else if (Files.isDirectory(Path.of(path))) {
                    validatePath.setError("The file must not be a directory");
                    throw new IssueTrackingException(validatePath.getError());

                }
                else if ( !Path.of(path).getFileName().toString().endsWith(".json")){
                    validatePath.setError("The file must have a .json extension");
                    throw  new IssueTrackingException(validatePath.getError());
                }
            }catch (InvalidPathException e){
                IssueTrackerImpl.printToConsole("An error occurred");
            }
            return validatePath.getError().isEmpty() ;
        }


        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }


        public static void isIssueExist(Issue issue) throws  IssueTrackingException {
            if (issue == null) {
                throw new IssueTrackingException("issue not exist");
            }

        }

    public static void validateInputToCreateIssue(String id, String name, Severity severity, Type type, String milestoneId) throws IssueTrackingException {
        if (!ValidationHelper.isId(id)) {
            throw new IssueTrackingException("An ID has to start with a letter followed by zero or more letters or numbers.");
        } else if (isNameNull(name) || isNameEmplty(name)) {
            throw new IssueTrackingException("Name must not be null or empty");
        } else if (severity == null || type == null) {
            throw new IssueTrackingException("Severity or type must not be null");
        } else if (milestoneId == null || milestoneId.isEmpty()) {
            throw new IssueTrackingException("Milestone ID must not be null or empty");
        }
    }
    public static void validateInputToCreateMilestone(String id, String name) throws IssueTrackingException {
        if (!ValidationHelper.isId(id)) {
            throw new IssueTrackingException("An ID has to start with a letter followed by zero or more letters or numbers.");
        }
        if (isIdEmpty(id) || isIdNull(id)) {
            throw new IssueTrackingException("ID must not be empty or null");
        }
        if (isNameNull(name) || isNameEmplty(name)) {
            throw new IssueTrackingException("Name must not be null or empty");
        }
    }

    public static void validateId(String id) throws IssueTrackingException {
        if (!ValidationHelper.isId(id)) {
            throw new IssueTrackingException("An ID has to start with a letter followed by zero or more letters or numbers.");
        }
    }

    public static void validateIssueIdsOrDependencies(Set<String> issueIdOrDependencies) throws IssueTrackingException {
        if (issueIdOrDependencies.contains("")) {
            throw new IssueTrackingException("The list must not contain an empty ID");
        }
    }




        public static  boolean isIdEmpty(String id){
            return id.isEmpty();
        }
        public static boolean isIdNull(String id){
            return id.equals("null");
        }

        public static boolean isNameNull(String name){
            return name == null;
        }

        public static  boolean isNameEmplty(String name){
            return name.isEmpty();
        }




    }


