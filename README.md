# Musalasoft Event Booking App
##### Author : Ukwuoma Emeka Paul

This is a REST API that allows users to create, find and reserve tickets for events, view and manage their reservations and to be notified before the event kickoff.

### Technologies
- Java
- Maven
- Springboot
- Thymeleaf (For templating)
- Docker
- Postman
- Swagger
- MySQL
- H2


### Requirements

You need the following to build and run the application:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.8.1](https://maven.apache.org) (Optional as code already contains maven wrapper)
- [H2](https://www.h2database.com/)
- [Docker](https://hub.docker.com/) (To start up a docker server)


## How to run

### Step 1 - clone project from [here](https://github.com/ChukwuEmeka1269/event-booking-system.git) 

```
git clone https://github.com/ChukwuEmeka1269/event-booking-system.git
```


### Step 2 - CD into the project directory
```
cd event-booking-system/
```

### Step 3 - Build And Run The Application

##### Navigate to the `src/main/resources/application-docker.properties` and set the following properties
- spring.mail.username (your preferred mail server username)
- spring.mail.password (the configured password for the mail)
- mail.name= (Preferred name to be displayed on sent mails)
- mail.from= (mail sender address. Could be set to noreply also)

execute the following commands:

```bash
./mvnw clean install     # Build the project
docker-compose build     # Build Docker image
docker-compose up        # Run the project using Docker
````

### Step 4 - Run Test
```
./mvnw test
```

### Additional Information
Kindly note that the application would start on `Port 9000` so endpoints would be accessible at
`http://localhost:9000/**`. Use the appropriate HTTP methods `GET` `POST` etc. to access endpoints with you preferred http client e.g Postman



For ease of use and to test the functionality, I have loaded test data into the database, and  I have made documentation of the endpoints here.

After running the Application, You can see the documentation on swagger and test the end points.

#### Full `SWAGGER` documentation can be found [here](http://localhost:9001/swagger-ui/index.html) and the `POSTMAN` documentation can be found [here](https://documenter.getpostman.com/view/15908398/2sA3e1DAgU).