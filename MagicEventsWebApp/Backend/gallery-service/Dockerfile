FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/gallery-service-0.0.1-SNAPSHOT.jar gallery-service.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "gallery-service.jar"]