FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/*.jar app.jar

COPY credentials ./credentials

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]