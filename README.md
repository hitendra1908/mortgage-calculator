# Mortgage-Calculator

It is an API that fetches all the interest rates and gives the monthly cost if person is eligible for mortgage.

## Technologies Used
This project utilizes the following technologies:
* Spring Boot 3.5.5
* Spring Data JPA
* PostgreSQL 15
* Maven 3.9.7
* Java 21
* JUnit 5
* OpenAPI 3 for Swagger documentation
* Docker
* [REST Assured](https://rest-assured.io/) and [Testcontainers](https://testcontainers.com/) (for Spring integration tests using a container)

## Project Structure
Project has number of files/folders, below is the explanation for each of them:

1. **src** -> source code of the mortgage-calculator.
2. **postman** -> contains postman collection json file to test endpoint via Postman.
3. **docker-compose.yml** -> compose file to create docker container of database.

## Swagger Documentation
Swagger docs will be available at : http://localhost:8080/swagger-ui/index.html
You can also download Json & Yaml file from http://localhost:8080/v3/api-docs and http://localhost:8080/v3/api-docs.yaml respectively.

## How to Run the Project in Local

1. Clone the repository:
   ```sh
   git clone https://github.com/hitendra1908/mortgage-calculator.git

2. To run the application - Navigate to the root directory and start Docker:
   ```sh
   docker compose up

3. Build the project:
   ```sh
   mvn clean install

4. Run the Spring Boot application:
   ```sh
   mvn spring-boot:run

Application runs on localhost:8080

## How to Access API in Local

Access API using http://localhost:8080/api/interest-rates

## Test Endpoints via Postman
Postman collection is available at "postman" folder to test endpoints via Postman.
