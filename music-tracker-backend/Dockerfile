FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache wget && \
    addgroup -S spring && adduser -S spring -G spring

USER spring:spring

COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
