FROM eclipse-temurin:17-jdk-alpine
ADD target/*.jar app.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","/app.jar"]
