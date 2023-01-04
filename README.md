# Spring Boot User Authentication

## Overview
A simple service that provides the user with the functionality of authentication.

- Languages and Frameworks:
    - Java
    - Spring Boot
    - Lombok
- Infra
    - Docker
- Storage
    - H2 Database

## Owner
|   |   |
| :---: | :---: |
| **Name** | Muhammad Bilal Khan |
| **Email** | [m.bilal.iqbal.98@gmail.com](m.bilal.iqbal.98@gmail.com) |
| **Contact** | +923321309392 | 

How to start the application
---

### On Docker

Our integration tests 
1. Run `docker pull muhammadbilalkhan/spring-boot-user-authentication` to pull the deployed docker image
1. Start application with `docker run -p 9090:8080 muhammadbilalkhan/spring-boot-user-authentication`
1. To check that your application is running enter url `http://localhost:9090/`

### On Local

Our integration tests 
1. Run `mvn clean install` to build your application
1. Start application with `mvn spring-boot:run`
1. To check that your application is running enter url `http://localhost:8080/`

Health Check
---

To see your applications' health enter url `http://localhost:<port>/`

## Endpoint
|             |                               |
|:-----------:|:-----------------------------:|
|  **Home**   |     GET localhost:\<port>     |
|  **Signup** | POST localhost:\<port>/signup |
|  **Login**  | POST localhost:\<port>/login  |
|  **Hello**  |  GET localhost:\<port>/hello  |

REST Resources
---

### Welcome

GET / (com.callsign.userauthentication.controller.AuthController)</br>

 ```$xslt
curl --location --request GET 'localhost:8080/'
```

### Signup to create new user

POST /signup (com.callsign.userauthentication.controller.AuthController)</br>

 ```$xslt
curl --location --request POST 'localhost:8080/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "Bilal",
    "password": "password123"
}'
```

### Login to generate JWT token

POST /login (com.callsign.userauthentication.controller.AuthController)</br>

 ```$xslt
curl --location --request POST 'localhost:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "Bilal",
    "password": "password123"
}'
```

JWT token received in the response of Login will be used in the Authorization header of below Greetings(GET /hello)

### Grettings (authenticated after login)

GET /hello (com.callsign.userauthentication.controller.AuthController)</br>

 ```$xslt
curl --location --request GET 'localhost:8080/hello?name=Bilal' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCaWxhbCIsImV4cCI6MTY3MjcxNDU5MiwiaWF0IjoxNjcyNjc4NTkyfQ.4YucExP5HnVDXQ76b0TH5cKsBfc620jSUVW_PPsGoRw'
```
