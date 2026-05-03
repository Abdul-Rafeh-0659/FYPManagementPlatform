package fyp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*
 * SDA Assignment 3
 * Final Year Project Management Platform
 *
 * This implementation focuses only on the functional requirements
 * connected to the selected 3 core use cases:
 *
 * 1. Student registers a final year project and submits a proposal.
 * 2. Student submits a project report.
 * 3. Supervisor reviews the report and provides feedback.
 *
 * Login is included as a supporting function so the two actors can access
 * their screens.
 */

public class FYPManagementPlatformApp extends JFrame{
        private final Repository repo=new Repository();
        private User currentUser;

        private final CardLayout cardLayout=new CardLayout();
        private final JPanel mainPanel=new JPanel(cardLayout);

        private final LoginPanel loginPanel=new LoginPanel();
        private final StudentPanel studentPanel=new StudentPanel();
        private final SupervisorPanel supervisorPanel=new SupervisorPanel();

        public FYPManagementPlatformApp(){
                setTitle("Final Year Project Management Platform");
                setSize(900,600);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setLocationRelativeTo(null);

                repo.loadSampleData();

                mainPanel.add(loginPanel,"login");
                mainPanel.add(studentPanel,"student");
                mainPanel.add(supervisorPanel,"supervisor");

                add(mainPanel);
                cardLayout.show(mainPanel,"login");
        }

        public static void main(String[] args){
                SwingUtilities.invokeLater(() -> new FYPManagementPlatformApp().setVisible(true));
        }

        private void showMessage(String message){
                JOptionPane.showMessageDialog(this,message);
        }

        private void login(String username,String password){
                if(username.trim().isEmpty()||password.trim().isEmpty()){
                        showMessage("Please enter both username and password.");
                        return;
                }

                User user=repo.findUser(username.trim(),password.trim());

                if(user==null){
                        showMessage("Invalid username or password.");
                        return;
                }

                currentUser=user;

                if(user instanceof Student){
                        studentPanel.refresh();
                        cardLayout.show(mainPanel,"student");
                }else if(user instanceof Supervisor){
                        supervisorPanel.refresh();
                        cardLayout.show(mainPanel,"supervisor");
                }
        }

        private void logout(){
                currentUser=null;
                loginPanel.clearFields();
                cardLayout.show(mainPanel,"login");
        }

        class LoginPanel extends JPanel{
                private final JTextField usernameField=new JTextField(20);
                private final JPasswordField passwordField=new JPasswordField(20);

                LoginPanel(){
                        setLayout(new GridBagLayout());
                        setBorder(new EmptyBorder(20,20,20,20));

                        GridBagConstraints c=new GridBagConstraints();
                        c.insets=new Insets(8,8,8,8);
                        c.fill=GridBagConstraints.HORIZONTAL;

                        JLabel title=new JLabel("Final Year Project Management Platform",SwingConstants.CENTER);
                        title.setFont(new Font("Arial",Font.BOLD,24));

                        JLabel help=new JLabel("Demo users: student1 / 123, supervisor1 / 123",SwingConstants.CENTER);

                        JButton loginButton=new JButton("Login");
                        loginButton.addActionListener(e -> login(usernameField.getText(),new String(passwordField.getPassword())));

                        c.gridx=0;
                        c.gridy=0;
                        c.gridwidth=2;
                        add(title,c);

                        c.gridy=1;
                        add(help,c);

                        c.gridwidth=1;
                        c.gridy=2;
                        c.gridx=0;
                        add(new JLabel("Username:"),c);
                        c.gridx=1;
                        add(usernameField,c);

                        c.gridy=3;
                        c.gridx=0;
                        add(new JLabel("Password:"),c);
                        c.gridx=1;
                        add(passwordField,c);

                        c.gridy=4;
                        c.gridx=0;
                        c.gridwidth=2;
                        add(loginButton,c);
                }

                void clearFields(){
                        usernameField.setText("");
                        passwordField.setText("");
                }
        }

        abstract class DashboardPanel extends JPanel{
                DashboardPanel(String title){
                        setLayout(new BorderLayout());

                        JPanel top=new JPanel(new BorderLayout());
                        top.setBorder(new EmptyBorder(10,10,10,10));

                        JLabel titleLabel=new JLabel(title);
                        titleLabel.setFont(new Font("Arial",Font.BOLD,20));

                        JButton logoutButton=new JButton("Logout");
                        logoutButton.addActionListener(e -> logout());

                        top.add(titleLabel,BorderLayout.WEST);
                        top.add(logoutButton,BorderLayout.EAST);

                        add(top,BorderLayout.NORTH);
                }
        }

        class StudentPanel extends DashboardPanel{
                private final JLabel projectInfoLabel=new JLabel("No project registered yet.");
                private final JTextArea feedbackArea=new JTextArea();

                StudentPanel(){
                        super("Student Dashboard");

                        JTabbedPane tabs=new JTabbedPane();

                        JPanel projectTab=new JPanel(new BorderLayout(8,8));
                        projectTab.setBorder(new EmptyBorder(10,10,10,10));
                        projectInfoLabel.setFont(new Font("Arial",Font.PLAIN,15));

                        JButton registerButton=new JButton("Register Project / Submit Proposal");
                        registerButton.addActionListener(e -> registerProject());

                        projectTab.add(projectInfoLabel,BorderLayout.CENTER);
                        projectTab.add(registerButton,BorderLayout.SOUTH);

                        JPanel reportTab=new JPanel(new BorderLayout(8,8));
                        reportTab.setBorder(new EmptyBorder(10,10,10,10));

                        JTextArea reportArea=new JTextArea();
                        reportArea.setLineWrap(true);
                        reportArea.setWrapStyleWord(true);

                        JButton submitReportButton=new JButton("Submit Report");
                        submitReportButton.addActionListener(e -> submitReport(reportArea));

                        reportTab.add(new JLabel("Write your progress report:"),BorderLayout.NORTH);
                        reportTab.add(new JScrollPane(reportArea),BorderLayout.CENTER);
                        reportTab.add(submitReportButton,BorderLayout.SOUTH);

                        JPanel feedbackTab=new JPanel(new BorderLayout(8,8));
                        feedbackTab.setBorder(new EmptyBorder(10,10,10,10));

                        feedbackArea.setEditable(false);
                        feedbackArea.setLineWrap(true);
                        feedbackArea.setWrapStyleWord(true);

                        JButton refreshButton=new JButton("Refresh Feedback");
                        refreshButton.addActionListener(e -> refreshFeedback());

                        feedbackTab.add(new JLabel("Supervisor Feedback:"),BorderLayout.NORTH);
                        feedbackTab.add(new JScrollPane(feedbackArea),BorderLayout.CENTER);
                        feedbackTab.add(refreshButton,BorderLayout.SOUTH);

                        tabs.add("Register Project",projectTab);
                        tabs.add("Submit Report",reportTab);
                        tabs.add("View Feedback",feedbackTab);

                        add(tabs,BorderLayout.CENTER);
                }

                void refresh(){
                        Student student=(Student)currentUser;
                        Project project=repo.findProjectByStudent(student);

                        if(project==null){
                                projectInfoLabel.setText("No project registered yet.");
                        }else{
                                projectInfoLabel.setText("<html><b>Project:</b> "+project.getTitle()+"<br><b>Proposal:</b> "+project.getProposal()+"</html>");
                        }

                        refreshFeedback();
                }

                private void registerProject(){
                        Student student=(Student)currentUser;

                        JTextField titleField=new JTextField();
                        JTextArea proposalArea=new JTextArea(6,25);
                        proposalArea.setLineWrap(true);
                        proposalArea.setWrapStyleWord(true);

                        JPanel panel=new JPanel(new GridLayout(0,1,6,6));
                        panel.add(new JLabel("Project title:"));
                        panel.add(titleField);
                        panel.add(new JLabel("Project proposal / description:"));
                        panel.add(new JScrollPane(proposalArea));

                        int result=JOptionPane.showConfirmDialog(this,panel,"Register Project",JOptionPane.OK_CANCEL_OPTION);

                        if(result!=JOptionPane.OK_OPTION){
                                return;
                        }

                        String title=titleField.getText().trim();
                        String proposal=proposalArea.getText().trim();

                        if(title.isEmpty()||proposal.isEmpty()){
                                showMessage("Project title and proposal cannot be empty.");
                                return;
                        }

                        Project existingProject=repo.findProjectByStudent(student);

                        if(existingProject!=null){
                                existingProject.setTitle(title);
                                existingProject.setProposal(proposal);
                                showMessage("Project proposal updated successfully.");
                        }else{
                                Supervisor supervisor=repo.getSupervisor();
                                Project project=new Project(repo.nextProjectId(),title,proposal,student,supervisor);
                                repo.projects.add(project);
                                showMessage("Project registered and proposal submitted successfully.");
                        }

                        refresh();
                }

                private void submitReport(JTextArea reportArea){
                        Student student=(Student)currentUser;
                        Project project=repo.findProjectByStudent(student);

                        if(project==null){
                                showMessage("Please register a project before submitting a report.");
                                return;
                        }

                        String content=reportArea.getText().trim();

                        if(content.isEmpty()){
                                showMessage("Report content cannot be empty.");
                                return;
                        }

                        Report report=new Report(repo.nextReportId(),content,project);
                        repo.reports.add(report);

                        showMessage("Report submitted successfully.");
                        reportArea.setText("");
                }

                private void refreshFeedback(){
                        Student student=(Student)currentUser;
                        Project project=repo.findProjectByStudent(student);

                        if(project==null){
                                feedbackArea.setText("No project registered yet.");
                                return;
                        }

                        StringBuilder sb=new StringBuilder();

                        for(Feedback feedback:repo.feedbackList){
                                if(feedback.getProject()==project){
                                        sb.append(feedback).append("\n\n");
                                }
                        }

                        if(sb.length()==0){
                                feedbackArea.setText("No feedback available yet.");
                        }else{
                                feedbackArea.setText(sb.toString());
                        }
                }
        }

        class SupervisorPanel extends DashboardPanel{
                private final JComboBox<Project> projectBox=new JComboBox<>();
                private final JTextArea proposalArea=new JTextArea();
                private final JTextArea reportArea=new JTextArea();
                private final JTextArea feedbackArea=new JTextArea();

                SupervisorPanel(){
                        super("Supervisor Dashboard");

                        JPanel main=new JPanel(new BorderLayout(8,8));
                        main.setBorder(new EmptyBorder(10,10,10,10));

                        JPanel top=new JPanel(new BorderLayout(5,5));
                        top.add(new JLabel("Select Student Project:"),BorderLayout.NORTH);
                        top.add(projectBox,BorderLayout.CENTER);

                        projectBox.addActionListener(e -> loadSelectedProject());

                        proposalArea.setEditable(false);
                        proposalArea.setLineWrap(true);
                        proposalArea.setWrapStyleWord(true);

                        reportArea.setEditable(false);
                        reportArea.setLineWrap(true);
                        reportArea.setWrapStyleWord(true);

                        feedbackArea.setLineWrap(true);
                        feedbackArea.setWrapStyleWord(true);

                        JTabbedPane tabs=new JTabbedPane();
                        tabs.add("Review Proposal",new JScrollPane(proposalArea));
                        tabs.add("Review Report",new JScrollPane(reportArea));
                        tabs.add("Write Feedback",new JScrollPane(feedbackArea));

                        JButton saveFeedbackButton=new JButton("Save Feedback");
                        saveFeedbackButton.addActionListener(e -> saveFeedback());

                        main.add(top,BorderLayout.NORTH);
                        main.add(tabs,BorderLayout.CENTER);
                        main.add(saveFeedbackButton,BorderLayout.SOUTH);

                        add(main,BorderLayout.CENTER);
                }

                void refresh(){
                        projectBox.removeAllItems();

                        Supervisor supervisor=(Supervisor)currentUser;

                        for(Project project:repo.projects){
                                if(project.getSupervisor()==supervisor){
                                        projectBox.addItem(project);
                                }
                        }

                        loadSelectedProject();
                }

                private void loadSelectedProject(){
                        Project project=(Project)projectBox.getSelectedItem();

                        if(project==null){
                                proposalArea.setText("No student projects available.");
                                reportArea.setText("No report available.");
                                feedbackArea.setText("");
                                return;
                        }

                        proposalArea.setText(project.getProposal());

                        StringBuilder reports=new StringBuilder();

                        for(Report report:repo.reports){
                                if(report.getProject()==project){
                                        reports.append(report).append("\n\n");
                                }
                        }

                        if(reports.length()==0){
                                reportArea.setText("No reports submitted yet.");
                        }else{
                                reportArea.setText(reports.toString());
                        }

                        feedbackArea.setText("");
                }

                private void saveFeedback(){
                        Project project=(Project)projectBox.getSelectedItem();

                        if(project==null){
                                showMessage("No project selected.");
                                return;
                        }

                        boolean hasReport=false;

                        for(Report report:repo.reports){
                                if(report.getProject()==project){
                                        hasReport=true;
                                        break;
                                }
                        }

                        if(!hasReport){
                                showMessage("Feedback can be added after the student submits a report.");
                                return;
                        }

                        String comments=feedbackArea.getText().trim();

                        if(comments.isEmpty()){
                                showMessage("Feedback cannot be empty.");
                                return;
                        }

                        Feedback feedback=new Feedback(repo.nextFeedbackId(),project,(Supervisor)currentUser,comments);
                        repo.feedbackList.add(feedback);

                        showMessage("Feedback saved successfully.");
                        feedbackArea.setText("");
                }
        }
}

abstract class User{
        private final int userId;
        private final String fullName;
        private final String username;
        private final String password;

        User(int userId,String fullName,String username,String password){
                this.userId=userId;
                this.fullName=fullName;
                this.username=username;
                this.password=password;
        }

        public int getUserId(){
                return userId;
        }

        public String getFullName(){
                return fullName;
        }

        public String getUsername(){
                return username;
        }

        public boolean checkPassword(String password){
                return this.password.equals(password);
        }

        public String toString(){
                return fullName;
        }
}

class Student extends User{
        Student(int userId,String fullName,String username,String password){
                super(userId,fullName,username,password);
        }
}

class Supervisor extends User{
        Supervisor(int userId,String fullName,String username,String password){
                super(userId,fullName,username,password);
        }
}

class Project{
        private final int projectId;
        private String title;
        private String proposal;
        private final Student student;
        private final Supervisor supervisor;

        Project(int projectId,String title,String proposal,Student student,Supervisor supervisor){
                this.projectId=projectId;
                this.title=title;
                this.proposal=proposal;
                this.student=student;
                this.supervisor=supervisor;
        }

        public int getProjectId(){
                return projectId;
        }

        public String getTitle(){
                return title;
        }

        public void setTitle(String title){
                this.title=title;
        }

        public String getProposal(){
                return proposal;
        }

        public void setProposal(String proposal){
                this.proposal=proposal;
        }

        public Student getStudent(){
                return student;
        }

        public Supervisor getSupervisor(){
                return supervisor;
        }

        public String toString(){
                return projectId+" - "+title+" - "+student.getFullName();
        }
}

class Report{
        private final int reportId;
        private final String content;
        private final Project project;
        private final LocalDateTime submittedAt=LocalDateTime.now();

        Report(int reportId,String content,Project project){
                this.reportId=reportId;
                this.content=content;
                this.project=project;
        }

        public Project getProject(){
                return project;
        }

        public String toString(){
                return "Report #"+reportId+"\nSubmitted: "+submittedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+"\nContent: "+content;
        }
}

class Feedback{
        private final int feedbackId;
        private final Project project;
        private final Supervisor supervisor;
        private final String comments;
        private final LocalDateTime createdAt=LocalDateTime.now();

        Feedback(int feedbackId,Project project,Supervisor supervisor,String comments){
                this.feedbackId=feedbackId;
                this.project=project;
                this.supervisor=supervisor;
                this.comments=comments;
        }

        public Project getProject(){
                return project;
        }

        public String toString(){
                return "Feedback #"+feedbackId+" by "+supervisor.getFullName()+"\nDate: "+createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+"\nComments: "+comments;
        }
}

class Repository{
        final List<User> users=new ArrayList<>();
        final List<Project> projects=new ArrayList<>();
        final List<Report> reports=new ArrayList<>();
        final List<Feedback> feedbackList=new ArrayList<>();

        private int userCounter=1;
        private int projectCounter=1;
        private int reportCounter=1;
        private int feedbackCounter=1;

        void loadSampleData(){
                Student student=new Student(nextUserId(),"Abdul Rafeh","student1","123");
                Supervisor supervisor=new Supervisor(nextUserId(),"Dr. Umer Haroon","supervisor1","123");

                users.add(student);
                users.add(supervisor);
        }

        int nextUserId(){
                return userCounter++;
        }

        int nextProjectId(){
                return projectCounter++;
        }

        int nextReportId(){
                return reportCounter++;
        }

        int nextFeedbackId(){
                return feedbackCounter++;
        }

        User findUser(String username,String password){
                for(User user:users){
                        if(user.getUsername().equalsIgnoreCase(username)&&user.checkPassword(password)){
                                return user;
                        }
                }
                return null;
        }

        Project findProjectByStudent(Student student){
                for(Project project:projects){
                        if(project.getStudent()==student){
                                return project;
                        }
                }
                return null;
        }

        Supervisor getSupervisor(){
                for(User user:users){
                        if(user instanceof Supervisor){
                                return (Supervisor)user;
                        }
                }
                return null;
        }
}
