PAC Spring 2016 - PAC Back-end

This project contains the back-end code related for PAC course.

Pre-requisites:
- Java JDK 8 (add to OS path)
- Maven 3+ (add to OS path)
- Virtual box (5+)
- Vagrant (1.8+, add to OS path)

Building the back-end project only:
- cd to /voting and run "mvn clean package" -- this builds the fat jar file and runs all unit tests producing runnable .jar.

Building the entire application - back-end and front-end projects with needed scripts as zip archive:
- Install Node JS (4.4+) - needed to build front-end project.
- clone pac-front-end project in the same directory level as pac-back-end project, so that the folders are siblings. Use folder name "pac-front-end".
- cd to pac-back-end/dev-infrastructure and run "vagrant up". This can last a couple of minutes until the machine is set up.
- cd to pac-back-end/voting and run "mvn clean install".
- Get some coffee.
- the "voting-application.zip" is created under pac-back-end/voting/voting-application/target.

Usage:
In "voting-application.zip" there is a folder "docs" containing the short tutorial on how to run the application.

This project is developed as part of PRODYNA Architecture Certification (PAC) course held by Darko Krizic, CTO of PRODYNA AG.
