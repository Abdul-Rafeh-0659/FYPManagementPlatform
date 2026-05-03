# Final Year Project Management Platform

This is the final Java GUI implementation for SDA Assignment 3.

## Project Scope

This project intentionally implements only the functional requirements connected to the selected core use cases from the previous assignment.

The assignment asks the GUI to reflect at least 2 to 3 core use cases. Therefore, this implementation focuses on these 3 core use cases:

1. Student registers a final year project and submits a proposal.
2. Student submits a project report.
3. Supervisor reviews the report and provides feedback.

Login is included as a supporting function because students and supervisors need to access their own dashboards.

## Important Scope Note

This project does not implement every functional requirement from the full system specification.

It implements the functional requirements related to the selected 3 use cases. This matches the interpretation that the implementation phase should cover the selected core use cases from the previous design, not the entire full-scale platform.

Features such as examiner grading, admin user management, password reset, deadline reminders, meeting scheduling and activity reports are part of the larger full-system design, but they are outside this focused GUI implementation.

## Technology Used

- Java
- Swing GUI
- Object-Oriented Programming
- In-memory ArrayList storage

No database is required for this version.

## Demo Accounts

| Role | Username | Password |
|---|---|---|
| Student | student1 | 123 |
| Supervisor | supervisor1 | 123 |

## How to Compile

Open terminal in the main project folder and run:

```bash
javac -d out src/fyp/FYPManagementPlatformApp.java
```

## How to Run

```bash
java -cp out fyp.FYPManagementPlatformApp
```

## Main Demo Flow

Use this flow in the project demo video:

1. Login as `student1`.
2. Register a final year project and submit the proposal.
3. Submit a project report.
4. Logout.
5. Login as `supervisor1`.
6. Select the student project.
7. Review the proposal.
8. Review the submitted report.
9. Write and save feedback.
10. Logout.
11. Login again as `student1`.
12. Open View Feedback and refresh the feedback.

## Functional Requirements Covered

This implementation covers the requirements connected to the selected use cases:

| Requirement | Status |
|---|---|
| FR2: Users can log in | Implemented |
| FR3: Student registers final year project | Implemented |
| FR4: Student submits project proposal | Implemented as proposal text during project registration |
| FR7: Student submits project report | Implemented |
| FR8: Supervisor reviews submitted report | Implemented |
| FR9: Supervisor provides written feedback | Implemented |
| FR10: Student views supervisor feedback | Implemented |

## Design-to-Code Mapping

The implementation uses classes that match the previous design:

- `User`
- `Student`
- `Supervisor`
- `Project`
- `Report`
- `Feedback`
- `Repository`
- `FYPManagementPlatformApp`

The design mapping is visible in the code:

- `Student` and `Supervisor` inherit from `User`.
- `Project` is associated with a `Student` and a `Supervisor`.
- `Report` is associated with a `Project`.
- `Feedback` is associated with a `Project` and a `Supervisor`.
- `Repository` stores users, projects, reports and feedback.

## Error Handling

The application handles basic errors using friendly messages:

- Empty username or password
- Invalid login
- Empty project title
- Empty project proposal
- Submitting a report before registering a project
- Empty report content
- Supervisor adding feedback before a report exists
- Empty feedback
- No project selected

## Notes for Submission

Upload this full project folder to a public GitHub repository.

The repository should include:

- `src/fyp/FYPManagementPlatformApp.java`
- `README.md`
- Demo video link or demo instructions
- Reflection notes or reflection video if required by the instructor

This project is intentionally focused and easy to explain. It demonstrates Java GUI implementation, design-to-code mapping, selected functional requirements and basic error handling.
