Perfect 👍 — let’s build a **simple GraphQL application in Spring Boot** that **calls another REST endpoint** (downstream service).

We’ll use **Spring Boot 3 + Java 21 + spring-boot-starter-graphql**.

---

## 🧩 Project Overview

| Layer                    | Description                                  |
| ------------------------ | -------------------------------------------- |
| **Controller (GraphQL)** | Exposes a GraphQL endpoint (`/graphql`)      |
| **Service**              | Calls an external REST API using `WebClient` |
| **Model**                | Defines the `User` type                      |
| **Schema**               | Defines GraphQL query for fetching users     |

---

## 📁 Folder Structure

```
graphql-rest-client/
├── src/
│   ├── main/
│   │   ├── java/com/example/graphql/
│   │   │   ├── GraphqlApplication.java
│   │   │   ├── model/User.java
│   │   │   ├── service/UserService.java
│   │   │   └── resolver/UserQueryResolver.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── schema.graphqls
└── pom.xml
```

---

## 🧠 `pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>graphql-rest-client</artifactId>
    <version>1.0.0</version>
    <name>graphql-rest-client</name>

    <properties>
        <java.version>21</java.version>
        <spring.boot.version>3.3.3</spring.boot.version>
    </properties>

    <dependencies>
        <!-- Spring Boot GraphQL -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-graphql</artifactId>
        </dependency>

        <!-- WebClient -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 🧱 `GraphqlApplication.java`

```java
package com.example.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraphqlApplication {
    public static void main(String[] args) {
        SpringApplication.run(GraphqlApplication.class, args);
    }
}
```

---

## 🧩 `model/User.java`

```java
package com.example.graphql.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String email;
}
```

---

## ⚙️ `service/UserService.java`

```java
package com.example.graphql.service;

import com.example.graphql.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    public List<User> getAllUsers() {
        return webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .block();
    }

    public User getUserById(int id) {
        return webClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }
}
```
   

## ▶️ Run the App

```bash
mvn spring-boot:run
```

Then open [http://localhost:8080/graphiql](http://localhost:8080/graphiql)

---

## 🧠 Example Queries

**1️⃣ Get all users**

```graphql
query {
  users {
    id
    name
    email
  }
}
```

**2️⃣ Get a single user**

```graphql
query {
  user(id: 2) {
    name
    email
  }
}
```

---

## ✅ What’s Happening

1. GraphQL `/graphql` endpoint receives query.
2. Spring GraphQL routes it to `UserQueryResolver`.
3. Resolver calls `UserService`.
4. `UserService` uses **WebClient** to call external REST API (`jsonplaceholder`).
5. Response mapped to `User` objects and returned to GraphQL client.

---

TODO : **persist data (Mutation)** — e.g., creating a user and posting to another API endpoint
