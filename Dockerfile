FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ADD target/*.jar app.jar
ENTRYPOINT [ "sh", "-c", "java "-Dspring.profiles.active=prod" -jar /app.jar" ]