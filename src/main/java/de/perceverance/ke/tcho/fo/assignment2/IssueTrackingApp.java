package de.perceverance.ke.tcho.fo.assignment2;

import de.perceverance.ke.tcho.fo.assignment2.issuetracking.*;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.logic.IssueTrackerImpl;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.logic.ProjectServiceImpl;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.model.*;
import de.perceverance.ke.tcho.fo.assignment2.issuetracking.view.ConsoleHelper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class IssueTrackingApp {
    ConsoleHelper console;
    IssueTrackerImpl issueTracker;
    MilestonHelper milestonHelper;
    IssueHelper issueHelper;
    public ProjectServiceImpl projectService;
    public IssueTrackingApp(){
        console = ConsoleHelper.build();
        issueTracker = new IssueTrackerImpl();
        projectService = new ProjectServiceImpl();
        milestonHelper = new MilestonHelper();
        issueHelper = new IssueHelper();
    }

    public void printMainMenu() throws IOException, IssueTrackingException {
        System.out.println("(1) validation/load");
        System.out.println("(2) create new project");
        System.out.println("(0) exit the program");
        int number=this.console.askIntegerInRange("the number read to the consol muss be situate between" , 0,2);
        switch (number){
            case 1:
                loadJsonfile();
                printMainMenu();

                break;
            case 2:

                subMenu();
                break;
            case 0:
                break;

        }
    }

    public void subMenu() throws IOException, IssueTrackingException {
        printSubMenuInfo();
        int number = console.askIntegerInRange("Numbers are between:", 0, 11);
        switch (number){
            case 1:
                addMileston();
                subMenu();
            case 2:
                removeMilestonId();
                subMenu();
            case 3:
                printListMileston();
                subMenu();
            case 4:
                createIssue();
                subMenu();
            case 5:
                closeIssueById();
                subMenu();
            case 6:
                removeIssueId();
                subMenu();
            case 7:
                printIssueLIst();
                subMenu();
            case 8:
                printJsontoconsole();
                subMenu();
            case 9:
                saveJsonToFile();
                subMenu();
            case 10:
                closeAllIssue();
                subMenu();
            case 11:
                changeIssueStatus();
                subMenu();
            case 0:
                if(closeProgrameOrBackToMainMenu() == 0) {
                    printMainMenu();
                }
                break;
        }

    }






    public void printSubMenuInfo(){

        System.out.println("(1) add  mileston");
        System.out.println("(2) remove mileston");
        System.out.println("(3) list Mileston");
        System.out.println("(4) add issue");
        System.out.println("(5) close issue");
        System.out.println("(6) remove issue");
        System.out.println("(7) list issue");
        System.out.println("(8) printJson to console");
        System.out.println("(9) save json");
        System.out.println("(10) close all issue");
        System.out.println("(11) change issue status");
        System.out.println("(0) Back to main menu/or close the programe");




    }

    public int closeProgrameOrBackToMainMenu() throws IOException {
        System.out.println("(0) Back to main menu/or (1) close the programe");
        int number = console.askIntegerInRange("Numbers are between:", 0, 1);
        return number;
    }


    public void changeIssueStatus() throws IOException {
        String id = console.askString("entrez id du issue a fermer ");
        projectService.changeIssueStatus(id);
    }
    public void closeAllIssue() throws IOException {
        String id = console.askString("Enter the ID of the issue to close:");
        projectService.closeAllIssue(id);
    }

    public void closeIssueById() throws IOException, IssueTrackingException {
        String id = console.askString("Enter the ID of the issue to close:");
        projectService.closeIssueById(id);

    }
    public void addMileston() throws IOException, IssueTrackingException {
        String id = console.askString("Enter Milestone Id ");
        addId(id);
        String name = console.askString("Enter Milestone  Name ");
        addname(name);
        addIssueIds();
        // addIssue(projectService);
        projectService.createMilestone(milestonHelper.getId(),milestonHelper.getName(),milestonHelper.getIssueId());



    }


    public void saveJsonToFile() throws IOException {

        while(true){
            String file = console.askString("Enter the file name to save the JSON, or enter 0 to exit if you don't need to save:");
            try {
                projectService.saveJsonToFile(file);
            }catch (IssueTrackingException e){
                System.out.println(e.getMessage());
            }
            if(file.equals("0")){
                break;
            }

        }

    }

    public void loadJsonfile() throws IOException {
        while (true) {
            String file = console.askString("Enter the file name to save the JSON, or enter 0 to exit if you don't need to save:");
            try {
                if (file.equals("0")) {
                    break;
                }
                issueTracker.load(file);
            } catch (IssueTrackingException e) {
                System.out.println(e.getMessage());
            }

        }
    }
    public void createIssue() throws IOException, IssueTrackingException {
        String issueId = console.askString("Enter Issue Id");
        issueHelper.setId(issueId);
        String issueName = console.askString("Enter Issue Name");
        issueHelper.setName(issueName);
        issueHelper.setState(State.OPEN);
        chooseSeverity();
        chooseType();
        String milestonId = console.askString("Enter Milestone Id");
        issueHelper.setMileston(milestonId);
        issueHelper.setId(issueId);
        addIssueDependencies();
        projectService.createIssue(issueHelper.getId(),issueHelper.getName(),"",issueHelper.getSeverity(),issueHelper.getType(),issueHelper.getMileston(),issueHelper.getIssueList());

    }

    public void chooseSeverity() throws IOException {
        System.out.println("Enter 1 for Severity.MAJOR");
        System.out.println("Enter 2 for Severity.MINOR");
        System.out.println("Enter 3 for Severity.TRIVIAL");
        System.out.println("Enter 4 for Severity.CRITICAL");
        System.out.println("Enter 0 to exit the program");
        int number = console.askIntegerInRange("Enter a number between 1 and 4: ", 1, 4);
        switch (number){
            case 1:
                issueHelper.setSeverity(Severity.MAJOR);
                break;
            case 2:
                issueHelper.setSeverity(Severity.MINOR);
                break;
            case 3:
                issueHelper.setSeverity(Severity.TRIVIAL);
                break;
            case 4:
                issueHelper.setSeverity(Severity.CRITICAL);
                break;

        }

    }

    public void chooseType() throws IOException {
        System.out.println("Enter 1 for Type.BUG");
        System.out.println("Enter 2 for Type.FEATURE");

        int number = console.askIntegerInRange("Enter a number between 1 and 2: ", 1, 2);
        switch (number) {
            case 1:
                issueHelper.setType(Type.BUG);
                break;
            case 2:
                issueHelper.setType(Type.FEATURE);
                break;

        }
    }
    public void printJsontoconsole() throws IssueTrackingException {
        projectService.printJsonToConsole();
    }


    public void printListMileston() throws IssueTrackingException {
        projectService.printMilestonToConsole();
    }

    public void printIssueLIst(){
        projectService.printIssueToConsole();
    }

    public void removeMilestonId() throws IssueTrackingException, IOException {
        while(true){
            String id = console.askString("enter milestonId to remove or enter 0 to exit");
            if(id.equals( "0")){
                break;
            }
            projectService.removeMilestoneById(id);
        }


    }

    public void removeIssueId() throws IOException, IssueTrackingException {
        while(true){
            String id = console.askString("Enter milestone ID to remove, or enter 0 to exit:");
            if(id.equals( "0")){
                break;
            }
            projectService.removeIssueById(id);
        }

    }

    public Project createProject(){
        return new Project();

    }

    public void addId(String id){
        milestonHelper.setId(id);

    }

    public void addname(String name){
        milestonHelper.setName(name);
    }

    public void addIssueIds() throws IOException {
        Set<String> issueID = new HashSet<>();
        while(true){
            String id = console.askString("Enter the IDs, and type 'stop' to finish:");
            if(id.equals( "stop")){
                break;
            }
            issueID.add(id);
        }
        milestonHelper.setIssueId(issueID);
    }

    public void addIssueDependencies() throws IOException {
        Set<String> issueID = new HashSet<>();
        while(true){
            String Id = console.askString("Enter the IDs, and type 'stop' to finish:");
            if(Id.equals( "stop")){
                break;
            }
            issueID.add(Id);
        }
        issueHelper.setIssueList(issueID);
    }


    public static void start() throws IssueTrackingException, IOException {
        IssueTrackingApp issueTrackingApp = new IssueTrackingApp();
        issueTrackingApp.printMainMenu();

    }
}
