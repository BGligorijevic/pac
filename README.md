PAC Spring 2016 - PAC Back-end

This project contains the back-end code related for PAC course.

Pre-requisites:
- Java JDK 8 (add to OS path)
- Maven 3+ (add to OS path)
- Optional (needed to run integration tests): Check-out pac-infrastructure project and run "vagrant up", this will provide the necessary infrastructure.

Building the project:
- mvn clean package -- this will build the fat jar file and run unit tests.
- mvn clean install -- this build the the fat jar file, run unit tests and also run integration tests (you need pac-infrastructure in order to run integration tests)

Usage:
cd into pac-back-end/voting/voting-application/target and run java -jar voting-application-1.0-SNAPSHOT.jar. This will boot the Tomcat instance on localhost:8080 containing all the back-end logic exposed as REST services.

This project is developed as part of PRODYNA Architecture Certification (PAC) course held by Darko Krizic, CTO of PRODYNA AG.
