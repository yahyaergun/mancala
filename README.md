# Mancala Backend

### What
Backend API for mancala (kalah) board game - https://en.wikipedia.org/wiki/Mancala

### Features
* Java 11
* Spring Boot 2.2.4
* Spring Web (with embedded tomcat)
* Websockets (via spring-web-socket & stomp & sockjs)
* Spring Data JPA
* Embedded H2 database
* Lombok
* JUnit 5 (AssertJ, Mockito, Controller tests via `@WebMvcTest` and MockMvc)

### Tests

`mvn clean verify` to compile and run the tests

### How to run

`mvn spring-boot:run` from command-line or run as spring boot application from your favourite IDE
 
 Backend will start serving on `localhost:8080`

### Endpoints
POST `/games` -> Endpoint to create a game.

GET `/games` -> Retrieves games that are `IN_PROGRESS` or `WAITING_FOR_PLAYERS`

GET `/games/{id}/join` -> Joins the game as second player

POST `/games/{id}/move` -> Makes a move in the game

POST `/players` -> Login (create or fetch from database) with a name and set it on the session

POST `/players/logout` -> Removes the current player from its associated session

GET `/players/current` -> Gets the player currently associated with the session

