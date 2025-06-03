FROM maven:3.9.8-eclipse-temurin-8-alpine AS build
WORKDIR /app


COPY pom.xml .
RUN mvn -q dependency:go-offline


COPY src ./src
RUN mvn -q package -DskipTests

FROM eclipse-temurin:8-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*jar-with-dependencies.jar /app/app.jar


ENV BOARD_API=https://storage.googleapis.com/jobrapido-backend-test/board.json \
    COMMANDS_API=https://storage.googleapis.com/jobrapido-backendtest/commands.json

ENTRYPOINT ["java","-jar","/app/app.jar"]
