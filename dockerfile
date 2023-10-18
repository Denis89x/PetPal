FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/by.lebenkov-1.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]