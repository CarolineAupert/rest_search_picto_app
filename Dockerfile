FROM eclipse-temurin:17-jdk-alpine
ADD target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java "-Dspring.profiles.active=prod" -jar /app.jar" ]
