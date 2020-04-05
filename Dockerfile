FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

COPY app.properties app.properties
ENTRYPOINT ["java","-Dspring.profiles.active=local","-jar","/app.jar"]