FROM eclipse-temurin:21-jre
LABEL authors="range79"
COPY build/libs/app.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]