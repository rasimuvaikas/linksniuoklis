## Linksniuoklis 
An online learning tool for practising Lithuanian noun declension and accentuation

## Requirements

Linksniuoklis requires the following to run:

- Java JDK 8
- Node.js (https://nodejs.org/) and npm
- MySQL (https://www.mysql.com/)
- TomCat (http://tomcat.apache.org/)

## Setup

1. Unzip the Decliner/target/Decliner.war file into a directory
2. Locate the Decliner/WEB-INF/classes/dbconfig.properties file and replace the MySQL user login information with your own
3. Create databases and tables:
  * For Linux: Move into the WEB-INF directory, and run ```java -cp "lib/*:classes/." Data```
  * For Windows: Move into the WEB-INF directory, and run ```java -cp "lib/*;classes/." Data```
4. Rezip the directory and change the file ending to .war
5. Add the rezipped Decliner.war to Tomcat WebApps folder, run TomCat bin/startup
6. Run ```npm install```, and then ```ng serve``` commands inside the learnLT folder
5. Access the learning tool through localhost://4200
