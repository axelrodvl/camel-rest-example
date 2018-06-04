# Simple REST endpoint made with Camel, Spring and Spark.

## Running:
- Executable JAR with dependencies:

    ```
    mvn clean compile assembly:single
    java -jar target/camel-rest-0.1.0-SNAPSHOT-jar-with-dependencies.jar
    ```
- Testing purposes:

    ```
    mvn clean camel:run
    ```
    
## Endpoints:
- GET - returns Swagger endpoint description:

    ``
        http://localhost:9977/camel/swagger
    ``
- POST - returns greeting, which contains request body 

    ``
        http://localhost:9977/camel/hi
    ``
