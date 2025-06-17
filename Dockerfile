FROM ubuntu:latest
LABEL authors="zhand"

ENTRYPOINT ["top", "-b"]

FROM maven:3.8.2-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/UserService.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]