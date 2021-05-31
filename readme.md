#Simple-

This project is build with Java version8 and `SpringBoot` framework and built with `Gradle`. The `Gradle` wrapper is provided so no installation is needed.


## Build

Navigate to the root directory in command line of the project and run 

```
./gradlew build
```

This should bring in all required dependency and compile the source code

## Test

Run this command to execute all tests
```
./gradlew test
```

## Configuration

This app is configured through environmental variables.

```
ENABLE_H2_CONSOLE (true/false) 
```
Default: `true`. 

This app contain h2 in memory DB. This variable is used to turn on the sql console for h2
If enabled h2 console can be accessed at `localhost:8080/h2-console`

```
DB_URL
```
Default `jdbc:h2:mem:testdb`

The jdbc Url for DB. The default value will start a h2 in memory db instance for development

```
DB_DRIVE
```
Default `org.h2.Driver`

JDBC driver classpath to use. 

```
DB_USER
``` 
Default: `sa`

DB user name. 

```
DB_PASSWORD
```
Default: `password01`

Password for DB user 


```
spring.jpa.database-platform
```
Default: `org.hibernate.dialect.H2Dialect`

The Hibernate is used as the ORM. This is the classpath to tell which sql dialect to use

## Run the application

### From Gradle
Run this command to start the app within gradle

```
./gradle bootRun
```

### From Jar file

Run these command 
```
./gradlew build  
```
This command should build a jar file (java executable) in `build/libs` folder

```
java -jar build/libs/simple-address-book-0.0.1-SNAPSHOT.jar
```

This will start the app with the Jar executable


## Containerize 

A Dockerfile is provided to run the app within a docker container

Run these command to build the image 

```
./gradlew build
docker build --build-arg JAR_FILE=build/libs/\*.jar -t simple-address-book .
```

And run this command to create container

```
docker run -d -e “<environment variabes>” … -p <hostPort>:<Container port> -t simple-address-book
```

e.g.

```
docker run -d -e "DB_USER=sa" -e "DB_PASSWORD=pass"  -p 80:8080 -t simple-address-book
```
This should start the app in container at `localhost:80` of the host


