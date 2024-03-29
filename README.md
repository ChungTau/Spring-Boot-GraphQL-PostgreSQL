# Spring Boot GraphQL PostgreSQL Project

This is a demo project showcasing the integration of Spring Boot, GraphQL, and PostgreSQL.

## Prerequisites

Before running this project, make sure you have the following installed:

- Java Development Kit (JDK) 17
- Maven
- PostgreSQL

## Getting Started

1. Clone this repository:

   ```bash
   git clone https://github.com/ChungTau/Spring-Boot-GraphQL-PostgreSQL.git
   ```
   
2. Navigate to the project directory:
   ```bash
   cd demo
   ```
   
3. Build the project using Maven:
   ```bash
   mvn clean package
   ```
4. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   The application will start and be accessible at http://localhost:8081.
   
## Configuration
* The application uses PostgreSQL as the database. Make sure to configure the database connection in `application.properties`.
* GraphQL schemas are defined in the graphql directory.

## Usage
* Use GraphQL queries and mutations to interact with the API endpoints.
* The API endpoints are exposed at `/graphql`.

## Dependencies
* Spring Boot Starter Data JPA
* Spring Boot Starter GraphQL
* Spring Boot Starter Web
* PostgreSQL
* Lombok
* Spring Boot Starter Test
* Querydsl APT
* Querydsl JPA
* Spring GraphQL Test
* Spring Boot DevTools

## License
This project is licensed under the `MIT License`.