Added Spring Boot application startup event listeners to log all major startup events:

ApplicationStartingEvent - Application is starting

ApplicationEnvironmentPreparedEvent - Environment is prepared

ApplicationContextInitializedEvent - Context is initialized

ApplicationPreparedEvent - Application is prepared

ContextRefreshedEvent - Context is refreshed

ApplicationStartedEvent - Application has started

ApplicationReadyEvent - Application is ready to serve requests

Each event will be logged during the Spring Boot startup process.