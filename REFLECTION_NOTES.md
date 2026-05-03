# LLM Technical Reflection Notes

Use these notes to prepare the short reflection video.

## 1. Why did we choose this LLM?

We used ChatGPT because it understands Java Swing, object-oriented programming, UML-based class structures and software design concepts. It helped convert selected use cases from the previous design into working Java GUI code.

## 2. Good observations about the LLM-assisted approach

First, it helped generate the basic Java Swing GUI faster.

Second, it helped map the previous design into Java classes such as User, Student, Supervisor, Project, Report and Feedback.

Third, it helped add validation and friendly error messages.

## 3. Bad observations about the LLM-assisted approach

First, the code still needed manual review to make sure it followed the assignment scope.

Second, the LLM initially suggested extra features, so we had to reduce the project back to the selected 3 core use cases.

Third, the LLM cannot fully know the instructor's exact expectation, so the final scope had to be checked against the assignment requirements.

## 4. Recommended design patterns

The recommended patterns are MVC and Repository.

## MVC Pattern

MVC separates the user interface from data and logic. In this project, the model classes are User, Student, Supervisor, Project, Report and Feedback. The GUI is written using Swing panels.

The code would still work without MVC, but as the project grows, mixing all logic with the GUI would become harder to maintain.

One limitation is that full MVC would require more files and more structure, which may be unnecessary for this small assignment.

## Repository Pattern

The Repository pattern keeps stored data and search methods in one place.

In this project, the Repository class stores users, projects, reports and feedback using ArrayLists.

The project would still work without the Repository class, but data handling would be mixed directly into the GUI code.

One limitation is that this Repository only stores data in memory, so data is lost when the program closes.
