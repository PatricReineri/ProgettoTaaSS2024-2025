FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/board-service-0.0.1-SNAPSHOT.jar board-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "board-service.jar"]