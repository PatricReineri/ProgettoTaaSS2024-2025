FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/guest-game-service-0.0.1-SNAPSHOT.jar guest-game-service.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "guest-game-service.jar"]