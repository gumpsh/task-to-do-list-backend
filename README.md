# Task To-do List Backend
This is the backend API for the new task management system required for the Developer Technical Test.

## Getting Started
This is a Spring Boot application, using Gradle as the build tool.

### Prerequisites
Java version: 17 or higher\
The application currently uses a PostgreSQL database, but can be configured for other databases.\
For this, you will need to alter the following files:
* `src/main/resources/application.yaml` - Provide environment variables for the db driver, url, username and password.
* `src/main/resources/application.properties` - Set environment variables for the above.
* `build.gradle` - Include the appropriate dependencies.

Flyway db has been used for database migrations.\
If you wish to use an alternative - such as Liquibase - please remove the Flyway dependency from `build.gradle` and add appropriate dependencies.\
You will also need to update `src/main/resources/application.yaml` to remove the Flyway configuration and add appropriate configuration.

### Running the Application
You will need Gradle installed if building and running from the command line.\
Use `./gradlew build` to build the application.\
Use `./gradlew bootRun` to run the application.\

Otherwise, you can import the project into an IDE such as IntelliJ or Eclipse and run it from there.

The application will currently start on `http://localhost:4000`, but the port can be reconfigured in `src/main/resources/application.yaml`.

### API Documentation
The API documentation is available via Swagger UI at `http://localhost:4000/swagger-ui.html` once the application is running.
There are endpoints for:
* Fetching a list of tasks.
* Fetching a single task by ID.
* Creating a new task.
* Updating the status of a task.
* Deleting a task.
* Fetching a list of statuses (for populating dropdowns, etc).
* Fetching a list of tasks by supplied search criteria as url query parameters.

### Error Handling and Validation
There is an ErrorHandler class in the exception package that takes advantage of Spring's @RestControllerAdvice to handle exceptions globally.\
Custom exceptions have been created for:
* EntityNotFoundException (404).
* InvalidStatusException (400).

There is also an Error object to provide a more consistent and human-readable error response.

## Testing
Tests have been written using JUnit and Mockito.\
To run the tests, use `./gradlew test` from the command line, or run them from your IDE.
Tests include:
* Test fetching all tasks.
* Test fetching a task by ID.
* Test creating a new task.
* Test updating a task's status.
* Test deleting a task.
* Test fetching all statuses.
* Test searching for tasks by criteria.



