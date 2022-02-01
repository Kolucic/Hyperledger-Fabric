# HyperledgerFabric Wizard
This repo contains the wizard backend.
## Prerequisites
In order to run the server you need to have a Java compiler installed (java 11+).
## Run
```shell script
./gradlew bootRun # Compiles and runs the server

./gradlew bootJar # Compiles and creates a jar in ./build/libs
java -jar artifacts.jar # Executes the given jar
```
## Port configuration
By default the server is started on the 8080 port. 

In order to change that you need to add the following line to the `./src/main/resources/application.properties` file.
```shell script
server.port=your port
```