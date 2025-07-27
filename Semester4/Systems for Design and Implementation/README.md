# Triathlon Contest Management Projects

This repository contains my projects for the “Systems for Design and Implementation” course, based on a triathlon contest management scenario.

The main goal is to manage participants, judges (arbiters), and results for each stage of a triathlon competition. The system allows judges to log in, view and update participant scores for their assigned stages, and generate reports. The projects demonstrate desktop, client-server, REST, ORM, and web technologies.

## Projects

### 1. Java Project

A desktop and server-side application for managing the triathlon contest in Java.

**Features:**
- **Authentication:** Judges can log in to manage their stage.
- **Participants List:** Displays all participants and their total scores, sorted alphabetically.
- **Score Entry:** Allows judges to enter points for each participant after their stage ends. After submitting results, all connected judges see updated scores automatically.
- **Reports:** Judges can view a report showing participants with scores in their stage, sorted by points (descending).
- **Database Persistence:** Uses a relational database, with configuration in an external file.
- **Repository Pattern & Logging:** Implements data access using repositories and logs repository operations.
- **Networking:** Implements client-server synchronization with sockets and threads for real-time data updates across judges’ windows.
- **ORM:** Uses Hibernate to persist at least one domain entity.

### 2. C# Project

A desktop application that implements similar logic in C#.

**Features:**
- Data model and repository pattern with relational database persistence.
- Service and GUI layers for judge login, score entry, and reporting.
- Logging for repository/database operations.

### 3. Angular Web Client

A web application frontend for the Java project’s REST API, built using Angular.

**Features:**
- Connects to the Java backend via REST services.
- Allows users to view, add, update, and delete contest participants and results.
- Presents participant lists and enables judges to enter and modify scores through a user-friendly web interface.

## Implementation Highlights

- **Persistence:** Both Java and C# projects use relational databases for storing contest data.
- **ORM:** Hibernate is used in the Java project for object-relational mapping.
- **REST Services:** The Java backend exposes REST endpoints for the main entity types, supporting create, read, update, and delete operations.
- **Web Client:** The Angular app demonstrates modern web development practices and communicates with the backend via HTTP.
