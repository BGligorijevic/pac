PAC Spring 2016 - PAC Back-end

This project contains the back-end code related for PAC course.

Pre-requisites:
- Java JDK 8 (add to OS path)
- Maven 3+ (add to OS path)
- Virtual box (5+)
- Vagrant (1.8+, add to OS path)

Building the back-end project only:
- mvn clean package -- this builds the fat jar file and runs all unit tests
- mvn clean install -- this builds the the fat jar file, runs all unit tests, runs all integration tests (you need "vagrant up" in order to run integration tests) and packages both back-end and front-end applications as .zip archive

Building the entire application - back-end and front-end projects as zip archive:
- Install Node JS (4.4+)
- clone pac-front-end project in the same directory level as pac-back-end project, so that the folders are siblings. Use folder name "pac-front-end".
- cd to pac-back-end/dev-infrastructure and run "vagrant up". This can last a couple of minutes.
- cd to pac-back-end/voting and run "mvn clean install".
- get some coffee.
- the "voting-application.zip" is created under pac-back-end/voting/voting-application/target.

Usage:
In "voting-application.zip" there is a folder "docs" short tutorial on how to run the application.

This project is developed as part of PRODYNA Architecture Certification (PAC) course held by Darko Krizic, CTO of PRODYNA AG.
