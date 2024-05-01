# spring-boot-webflux-with-functional-programming
Spring Boot based Reactive RESTful API application which serves information about possible direct and interconnected flights (maximum 1 stop) based on the data consumed from external APIs.

# Tech Stack
- Java 21
- SpringBoot 3.0.7
- WebFlux
- Docker

# Instructions and Notes
Application can access using below mentioned url

[http://localhost:8080/api/v1/flight/interconnections?departure=DUB&arrival=WRO&departureDateTime=2024-04-03T07:00&arrivalDateTime=2024-04-03T20:26
](http://localhost:8080/api/v1/flight/interconnections?departure=STN&arrival=WRO&departureDateTime=2024-04-03T07:00&arrivalDateTime=2024-04-03T19:26)


Note: I have made the final response more readable and generic as follows while maintaing the requetsted response form (requested final response is under `data` section

![screenshot](image_1.jpg)

And also Some insights about unit and integration testing

![screenshot](image_2.jpg)

