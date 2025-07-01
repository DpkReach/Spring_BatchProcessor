# Spring Batch Demo

This is a sample Spring Boot application that demonstrates Spring Batch functionality with an in-memory H2 database.

## Features

- Spring Boot 4.x with Spring Batch
- H2 In-Memory Database
- REST API / Job Launch (customizable)
- Dockerized for container deployment

## How to Run

### Build the App

bash
mvn clean package -DskipTests

- /actuator/health → Health check
- /h2-console → H2 in-memory database
- /status → Custom status message

## Docker Commands

bash
docker build -t deepakadimoolam/spring-batch-demo .
docker run -p 8080:8080 deepakadimoolam/spring-batch-demo
