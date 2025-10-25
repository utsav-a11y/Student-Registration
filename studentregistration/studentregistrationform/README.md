Student Registration Form (Java Swing & MySQL)

This is a standalone desktop application built with Java Swing that provides a graphical user interface for registering student details. The application features robust input validation and stores the data securely in a MySQL database. This project is built and managed using Apache Maven.

✨ Features

Intuitive GUI: A clean and user-friendly form built with the Java Swing framework.

Input Validation: Ensures all fields are filled and validates the format for phone numbers and email addresses.

Database Integration: Securely connects to a MySQL database to store student records.

Error Handling: Provides clear, user-friendly pop-up messages for success, errors, and duplicate entries.

📋 Prerequisites

To run or build this application, you must have the following software installed.

1. Java Development Kit (JDK)

Requirement: JDK, version 8 or higher.

How to check: Open a terminal and type java -version.

Download: Oracle Java website.

2. Apache Maven

Requirement: Maven is used to build the project and manage dependencies.

How to check: Open a terminal and type mvn -version.

Download: Follow the installation guide on the official Maven website.

3. MySQL Server

Requirement: A running instance of MySQL Server.

Download: [suspicious link removed].

Important: Remember the root password you set during installation.

🛠️ Database Setup

Before running the application, ensure your MySQL server is running. The application is designed to be smart:

It will prompt you for your MySQL username and password on the first run.

It will automatically create the studentdb database if it doesn't exist.

It will automatically create the students table if it doesn't exist.

🏗️ How to Build and Run from Source

With Maven, building and running the project is a simple, two-step process.

Clone the repository:

git clone [https://github.com/your-username/your-repo.git](https://github.com/your-username/your-repo.git)
cd your-repo


Build the Project with Maven:
Open a terminal in the project's root directory (where the pom.xml file is) and run the following command:

mvn clean package


Maven will automatically download the required dependencies (like the MySQL driver), compile your code, and package it into a single, executable "fat JAR" in the target directory.

Run the Application:
Navigate to the target directory and run the application using the java -jar command.

cd target
java -jar StudentRegistrationForm-1.0-SNAPSHOT-jar-with-dependencies.jar
"# studentregistrationform" 
"# studentregistrationform" 
