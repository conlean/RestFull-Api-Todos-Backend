# Java, Maven, Spring Boot, HSQLDB, JPA, Hibernate Rest API

This is an example Restful CRUD ToDo API built on top of Spring Boot + HSQLDB as part of a challenge in the recruiting process.

## Requirements

1. Java 8

2. Maven 

3. Spring Boot


## Steps to Setup

**1. Download the zip file or clone the repository**

```bash
git clone https://github.com/conlean/RESTfulApi.git
```

**2. Run the app using maven**

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

## Explore Rest APIs

The app defines following CRUD APIs.
    
    GET localhost:8080 (Welcome, App running)

    GET localhost:8080/api/todos/   (All items)
    
    POST localhost:8080/api/todos/ (create new todo)
    
    GET localhost:8080/api/todos/{productId} (get item)
    
    GET localhost:8080/api/todos/search? (Get todos by description or state)
    
    PUT localhost:8080/api/todos/{productId} (update item)
    
    DELETE localhost:8080/api/todos/{productId} (delete item)

You can test them using postman or any other rest client.

    You must to add Cords to TodoController. 


## Key points to see as an example

+ API Crud implementation and validation of parameters.
+ Error handling to build the response (code + message).
+ Testing with mockito
+ File server implemented 

## Possible improvements / out of scope





