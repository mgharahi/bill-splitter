
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn clean install -DskipTests -q || true

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /bill
COPY --from=build /app/target/*.jar bill.jar
ENTRYPOINT ["java", "-jar", "bill.jar"]
