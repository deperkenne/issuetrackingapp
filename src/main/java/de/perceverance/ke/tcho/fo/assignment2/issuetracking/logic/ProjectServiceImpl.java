package de.perceverance.ke.tcho.fo.assignment2.issuetracking.logic;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.IssueTrackingException;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.ProjectService;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.*;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.validationInput.ValidatePath;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectServiceImpl implements ProjectService {
	public static final String RESET = "\u001B[0m";
	public static final String RED = "\u001B[31m";
	private static Project project ;
	public final List<String>listMilestonId = new ArrayList<>();;
	public final List<String>listIssueId = new ArrayList<>();;

	public ProjectServiceImpl(){

	}

	@Override
	public void createMilestone(String id, String name, Set<String> issueIds) throws IssueTrackingException {
		// TODO Auto-generated method stub#
		addAllIssueId();
		addAllMilestonId();
		Milestone milestone = new Milestone();

		try {
			ValidatePath.validateInputToCreateMilestone(id,name);
			ValidatePath.validateIssueIdsOrDependencies(issueIds);
			validateDuplicateIssueId(id);
			validateDuplicateMilestonId(id);
			validateIssueDependenciesIdExist(issueIds);
			milestone.setId(id);
			milestone.setName(name);
			milestone.setIssues(getIssueWithThisIds(issueIds));
			getProject().getMilestones().add(milestone);
		}catch (IssueTrackingException e){
			IssueTrackerImpl.printToConsole(e.getMessage());

		}
	}


	@Override
	public List<Milestone> getMilestones() {
		return getProject().getMilestones();
	}

	@Override
	public void removeMilestoneById(String id) throws IssueTrackingException {
		try {
			checkMilestonIdExist(id);
			listMilestonId.remove(id);
			Optional<Milestone> optionalIssue = getProject().getMilestones().stream().filter(milestone -> milestone.getId().equals(id)).findFirst();
			getProject().getMilestones().remove(optionalIssue.get());
			cascadeDeleteMileston(optionalIssue.get());
		}catch (IssueTrackingException e){
			IssueTrackerImpl.printToConsole(e.getMessage());
		}

	}

	public void cascadeDeleteMileston(Milestone mileston){
		for(Issue issuemileston :mileston.getIssues()){
			for(Issue issue : getIssues()){
				if(issue.getId().equals(issuemileston.getId())){
					issue.setMilestone(null);
				}


			}
		}
		mileston.getIssues().clear();
	}


	@Override
	public void createIssue(String id, String name, String description, Severity severity, Type type, String milestoneId, Set<String> dependencies) throws IssueTrackingException {
		addAllIssueId();
		addAllMilestonId();
		Issue issue = new Issue();

		try {
			ValidatePath.validateInputToCreateIssue(id,name,severity,type,milestoneId);
			validateDuplicateIssueId(id);
			checkMilestonIdExist(milestoneId);
			ValidatePath.validateIssueIdsOrDependencies(dependencies);
			validateIssueDependenciesIdExist(dependencies);
			issue.setId(id);
			issue.setName(name);
			issue.setDescription(description);
			issue.setSeverity(severity);
			issue.setType(type);
			issue.setState(State.OPEN);
			listIssueId.add(id);
			Milestone milestone = getMilestoneWithId(milestoneId);
			issue.setMilestone(milestone);
			issue.setDependencies(getIssueWithThisIds(dependencies));

			if(milestoneId.equals(" ")){
				System.out.println("dont add this issue to issuelist");
			}

			else {
				getProject().getIssues().add(issue);
			}
		}catch (IssueTrackingException e){
			System.out.println(e.getMessage());
		}
	}



	@Override
	public List<Issue> getIssues() {
		return getProject().getIssues();
	}

	@Override
	public void removeIssueById(String id) throws IssueTrackingException {
		try {
			checkIssueIdExist(id);
			listIssueId.remove(id);
			Optional<Issue> optionalIssue= getProject().getIssues().stream().filter(issue -> issue.getId().equals(id)).findFirst();
			getProject().getIssues().remove(optionalIssue.get());
			cascadeDeleteIssue(optionalIssue.get());


		}catch (IssueTrackingException e){
			IssueTrackerImpl.printToConsole(e.getMessage());
		}
	}

	// delete specific issue in all issuelist
	public void cascadeDeleteIssue(Issue issue){

		for(Issue currentIssue: getIssues()){
			if(currentIssue.getDependencies().contains(issue)){
				currentIssue.getDependencies().remove(issue);
			}

		}
		for(Milestone currentMilestone : getMilestones()){
			if(currentMilestone.getIssues().contains(issue)){
				currentMilestone.getIssues().remove(issue);
			}
		}

	}


	public void isAllissueClosed(String id)throws IssueTrackingException{
		Issue issueToclose = getIssueWithId(id);
		String issueNameNotClosed = "";
		for(Issue issue : issueToclose.getDependencies()){
			if (issue.getState() != State.CLOSED){
				issueNameNotClosed+= " "+issue.getId();
			}
		}

		if(!issueNameNotClosed.isEmpty()){
			throw new IssueTrackingException(RED+"issue with these ids must be close first:"+RESET+" "+issueNameNotClosed);
		}

	}
	@Override
	public void closeIssueById(String id) throws IssueTrackingException {

		try {
			 ValidatePath.validateId(id);
			 checkIssueIdExist(id);
			 isAllissueClosed(id);
			 Issue issue = getIssueWithId(id);
			 issue.setState(State.CLOSED);
		}catch(IssueTrackingException e){
			IssueTrackerImpl.printToConsole(e.getMessage());
		}

	}


	@Override
	public void printJsonToConsole() throws IssueTrackingException {
		// TODO Auto-generated method stub
		IssueTrackerImpl.printToConsole("Printing JSON file to console");
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			String json = mapper.writeValueAsString(getProject());
			if(json == null || json.isEmpty()){
				throw new IssueTrackingException("The JSON string must not be null or empty");
			}
			System.out.println(json);
		}catch (IOException e){
			System.out.println("An error occurred during JSON processing");
		}



	}


	@Override
	public void saveJsonToFile(String path) throws IssueTrackingException {
		ObjectMapper objectMapper1 = new ObjectMapper();
		ObjectWriter objectWriter1 = objectMapper1.writerWithDefaultPrettyPrinter();
		try {
			ValidatePath.pathToValid(path);
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(path));
			objectWriter1.writeValue(writer2, this.project);
		}catch(IssueTrackingException e){
			System.out.println(e.getMessage());
		} catch(IOException e){
			System.out.println("Error occurred while writing to file: " + e.getMessage());

		}


	}

	public  void printIssueToConsole(){
		String allIssue = "";
		for ( Issue issue : this.project.getIssues() ){
			allIssue += issue.toString()+"\n";
		}

		System.out.println(allIssue);

	}



	public void closeAllIssue(String id){
		try {
			ValidatePath.validateId(id);
			checkIssueIdExist(id);
			Issue issue = getIssueWithId(id);
			if (issue.getState() == State.CLOSED) {
				IssueTrackerImpl.printToConsole("The issue already has the status 'closed'.");
			}
			else {
				if (issue.getDependencies().isEmpty()) {
					issue.setState(State.CLOSED);
				}
				else if (issue.getDependencies().size() == 1) {
					if (issue.getDependencies().getFirst().getState() == State.OPEN) {
						issue.getDependencies().getFirst().setState(State.CLOSED);
						issue.setState(State.CLOSED);

					} else {
						issue.setState(State.CLOSED);
					}
				}
				else{
					for(Issue issue1 : issue.getDependencies()){
						if(issue1.getState() == State.OPEN){
							issue1.setState(State.CLOSED);
						}
					}
					issue.setState(State.CLOSED);
				}
			}

		}catch(IssueTrackingException e){
			IssueTrackerImpl.printToConsole(e.getMessage());
		}


	}

	public void printMilestonToConsole() {
		String mileston = "";
		for (Milestone milestone : this.project.getMilestones()) {
			mileston += milestone.toString()+"\n";
		}
		System.out.println(mileston);
	}

	public void changeIssueStatus(String id){
		try {
			ValidatePath.validateId(id);
			checkIssueIdExist(id);
			Issue issue = getIssueWithId(id);
			issue.setState(State.OPEN);
		}catch (IssueTrackingException e){
			System.out.println(RED + "Error occurred during validation: " + RESET + e.getMessage());
		}
	}

	public void checkIssueIdExist(String id)throws IssueTrackingException{
		if(!listIssueId.contains(id)){
			throw  new IssueTrackingException(RED+"This ID we tried to remove does not exist:"+RESET+" "+id);
		}

	}

	public void checkMilestonIdExist(String id)throws IssueTrackingException{
		String status ="";
		for(Milestone mileston : getMilestones()){
			if(mileston.getId().equals(id)){
				status = id;
				break;
			}
		}
		if(status.isEmpty()){
			throw  new IssueTrackingException(RED+"This Milestoneid not exist:"+RESET+ " "+id);
		}

	}



	// give allissue with them id is different to this id
	public  List<Issue> getIssueList (String id){
		return getIssues().stream()
				.filter(issue -> !issue.getId().equals(id))
				.collect(Collectors.toList());
	}

	public List<Issue> getIssueWithThisIds(Set<String>issueIds){
		List<Issue>issuelist = new ArrayList<>();
		for(String issueId : issueIds){
			for(Issue issue : getIssues()){
				if(issue.getId().equals(issueId)){
					issuelist.add(issue);
					break;
				}
			}
		}

		return issuelist;
	}


	public  Issue getIssueWithId(String id){
		Issue newissue = null;
		for(Issue issue : getIssues()){
			if(issue.getId().equals(id)){
				newissue = issue;
				break;

			}

		}
		return newissue;
	}



/*
	public void validationMilestonIdPresent(String id) throws IssueTrackingException{
		if(!listMilestonId.contains(id)){
			throw new IssueTrackingException("please choose another id this not exist");
		}

	}

	public void validateMilestonId(String id)throws IssueTrackingException{
		if(isMilestonIdPresent(id)){
			throw new IssueTrackingException("this given id is not valid");
		}


	}


 */

	public List<String> validateAndFindIssueWithId(Set<String>listContainIdTofind)throws IssueTrackingException{
		List<String> issueIds = new ArrayList<>();
		for(String idToFind : listContainIdTofind) {
			for (Issue issue : getIssues()) {
				if (issue.getId().equals(idToFind) ){
					issueIds.add(idToFind);
				}
			}
		}

		return issueIds;


	}


	/*

	public void validateIssuieIdsContainDuplicateId(Set<String> issuieIds)throws IssueTrackingException{
		String duplicatId = "";
		for(int i = 0; i<= issuieIds.size()-1;i++){
			for(int j = i+1 ; j<= issuieIds.size()-1;j++){
				if(issuieIds.toArray()[i] == issuieIds.toArray()[j]){
					duplicatId += " "+issuieIds.toArray()[i];
					j++;
				}else{
					j++;
				}

			}
			i++;
		}
		isIssueIdsContainDuplcat(duplicatId);

	}
*/

	public void validateIssueDependenciesIdExist(Set<String>listContainIdTofind)throws IssueTrackingException{
		//print specific id to user that not exist;
		String idNoExist="" ;
		Set<String> set2 = new HashSet<>(validateAndFindIssueWithId(listContainIdTofind));
		for (String item : listContainIdTofind) {
			if (!set2.contains(item)) {
				idNoExist += " "+item;
			}
		}
		if(!idNoExist.isEmpty()){
			throw new IssueTrackingException(RED+"This IssueId  not exist :"+RESET+ " "+ idNoExist );
		}
	}


	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;

	}

	public void addAllMilestonId(){
		for(Milestone milestone: getMilestones()){
			listMilestonId.add(milestone.getId());
		}

	}

	public void addAllIssueId(){
		for(Issue issue: getIssues()){
			listIssueId.add(issue.getId());
		}
	}
	public void validateDuplicateMilestonId(String id)throws IssueTrackingException{
		if(listMilestonId.contains(id)){
			throw new IssueTrackingException(RED + "The given milestone ID is already used by another milestone: " + RESET + id);

		}
	}

	public void validateDuplicateIssueId(String id)throws IssueTrackingException{
		String currentIssueId ="";
		if(listIssueId.contains(id)){
			currentIssueId = RED + "The given Issue ID is already used by another issue: " + RESET + id + "\n" + "For more information, try listing all issues.";
			throw new IssueTrackingException(currentIssueId);
		}
	}

	public Milestone getMilestoneWithId(String id){
		Milestone milestone = new Milestone();
		for(Milestone mileston: getMilestones()){
			if(mileston.getId().equals(id)){
				milestone = mileston;
				break;
			}
		}
		return milestone;
	}




}
