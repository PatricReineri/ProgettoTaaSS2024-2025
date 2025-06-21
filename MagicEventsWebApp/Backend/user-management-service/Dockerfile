FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/user-management-service-0.0.1-SNAPSHOT.jar user-management-service.jar

EXPOSE 8443

ENTRYPOINT ["java", "-jar", "user-management-service.jar"]
