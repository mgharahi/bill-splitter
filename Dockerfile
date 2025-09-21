FROM eclipse-temurin:21-jdk-alpine
WORKDIR /bill
COPY target/*.jar bill.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
