FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/*.jar /app/manager.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "manager.jar"]