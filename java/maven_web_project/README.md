# Maven Spring Boot Web Project

A minimal Spring Boot web application with REST API.

## Project Structure

```
maven_web_project/
├── pom.xml
├── src/main/
│   ├── java/com/example/
│   │   ├── Application.java
│   │   └── web/
│   │       ├── ApiController.java
│   │       └── HelloServlet.java
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml
│       └── index.html
└── README.md
```

## Build and Run

1. **Run Spring Boot application**:
   ```bash
   mvn spring-boot:run
   ```

2. **Or compile and run**:
   ```bash
   mvn clean package
   java -jar target/maven-web-project-1.0.0.jar
   ```

The application will be available at http://localhost:8080

## API Endpoints

- `/api/hello` - REST API hello endpoint (default greeting)
- `/api/hello?name=YourName` - REST API hello with custom name

## Actuator Endpoints 
Added Spring Boot Actuator with all endpoints exposed. Available endpoints include:



/actuator/health - Application health
/actuator/info - Application info
```shell
{"status":"UP"}
                                                                                                                                                                              
curl http://localhost:9090/actuator/info  
{}
```




/actuator/env - Environment properties
[Response](/assets/actuator_env.json)
/actuator/beans - Spring beans
[Response](/assets/actuator_beans.json)
/actuator/metrics - Application metrics

  GET http://localhost:9090/actuator/metrics/http.server.requests

GET http://localhost:9090/actuator/metrics/http.server.requests?tag=uri:/api/hello
GET http://localhost:9090/actuator/metrics/http.server.requests?tag=status:200
GET http://localhost:9090/actuator/metrics/http.server.requests?tag=method:GET


And many more at /actuator
```