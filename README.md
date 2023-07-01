# News management system

Implemented News management system as a microservice project

There are services:
* User microservice
* News-system microservice
* API Gateway microservice

`API Gateway` is program entry point, load balancer and responsible for security

`User microservice` is responsible for storing users, generating access and refresh tokens 

`News-system` is responsible for working with news and comments.

Technologies: 
* `Spring Boot 3.0.6, Java 17, Gradle, Spring (Cloud, Security, WebFlux), PostgreSQL, Redis, Docker, docker-compose, Swagger, Liquibase`

Implemented:
* `LRU, LFU caches`
* `Tests using Mockito and Testcontainers`
___
# __Project setup steps__
___
* ```git clone <username>/news-management-system.git ```
* go to the folder ```cd 'your project folder'```
* paste project url from the first step
* open the project in your IDE ```File->Open->'your project folder'```

# __To ```run``` application you need:__

* open folder with project in the terminal ```cd 'your project folder'```
* run command ```docker compose up -d --build```
## Endpoints for All (ADMIN, JOURNALIST, SUBSCRIBER):

* #### Authenticate endpoint `POST http://localhost:8080/authenticate/auth`

Request body:
  ```
  {
      "email":"your username",
      "password":"your password"
  }
```
* #### Refresh token endpoint `GET http://localhost:8080/authenticate/refresh`
* #### Register new user `POST http://localhost:8080/authenticate/register`

Request body:
  ```
  {
      "email":"your username",
      "password":"your password"
  }
```

* #### Find all news with comments `GET http://localhost:8080/full/find?keword=keyword`
* #### Find all news `GET http://localhost:8080/full/news?keword=keyword`
* #### Find all news as page `GET http://localhost:8080/full/news/page?keword=keyword&page=0&size=10`
* #### Find all comments `GET http://localhost:8080/full/comments?keword=keyword`
* #### Find all comments as page `GET http://localhost:8080/full/comments/page?keword=keyword&page=0&size=10`
## Endpoints for ADMIN: 

_In User microservice:_
* #### Find all users `GET http://localhost:8080/users`
* #### Find user by id `GET http://localhost:8080/users/<user's id>`
* #### Save new user `POST http://localhost:8080/users/save`
Request body:
  ```
  {
      "email":"your username",
      "password":"your password"
  }
```
* #### Update user `PUT http://localhost:8080/users/update/<user's id>`
Request body:
  ```
  {
      "email":"your username",
      "password":"your password"
  }
```
* #### Delete user by id `DELETE http://localhost:8080/users/delete/<user's id>`

_In News-system microservice:_

_News:_

* #### Find all news `GET http://localhost:8080/news`
* #### Find news by id `GET http://localhost:8080/news/<news id>`
* #### Find all news in page format `GET http://localhost:8080/news/page?page=0&size=10`
* #### Save new news `POST http://localhost:8080/news`
Request body:
  ```
  {
    "title":"title",
    "text":"text",
    "username": "username"
  }
```
* #### Update news `PUT http://localhost:8080/news/update/<news id>`
Request body:
  ```
  {
    "title":"title",
    "text":"new text"
  }
```
* #### Delete news by id `DELETE http://localhost:8080/news/delete/<news id>`

_Comments:_

* #### Find all news `GET http://localhost:8080/comment`
* #### Find news by id `GET http://localhost:8080/comment/<news id>`
* #### Find all comments by news id `GET http://localhost:8080/comment/{news id}/comments`
* #### Find all comments by news id as page `GET http://localhost:8080/comment/{news id}/comments/page`
* #### Save new comment `POST http://localhost:8080/comment`
Request body:
  ```
  {
    "text":"text",
    "username": "username"
  }
```
* #### Update comment `PUT http://localhost:8080/comment/update/<comment id>`
Request body:
  ```
  {
    "text":"text",
    "username": "username"
  }
```
* #### Delete comment by id `DELETE http://localhost:8080/comment/delete/<comment id>`
* #### Delete all comments by news id `DELETE http://localhost:8080/comment/{id}/comments`

## Endpoints for JOURNALIST:

* #### Save new news `POST http://localhost:8080/journalist/add`
Request body:
  ```
  {
    "title":"title",
    "text":"text",
  }
```
* #### Update news `PUT http://localhost:8080/journalist/update/<news id>`
Request body:
  ```
  {
    "title":"title",
    "text":"new text"
  }
```
* #### Delete news by id `DELETE http://localhost:8080/journalist/delete/<news id>`

## Endpoints for SUBSCRIBER:

* #### Save new comment `POST http://localhost:8080/comments/{news id}`
Request body:
  ```
  {
    "text":"text",
  }
```
* #### Update comment `PUT http://localhost:8080/comments/update/<comment id>`
Request body:
  ```
  {
    "text":"text",
  }
```
* #### Delete comment by id `DELETE http://localhost:8080/comments/delete/<comment id>`


