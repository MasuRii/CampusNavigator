# Multi-stage build: compile with Maven, run with minimal JRE
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Pre-download dependencies (ignore errors if network issues)
RUN mvn dependency:go-offline -B || true
COPY src ./src
RUN mvn -DskipTests package -B -e

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
