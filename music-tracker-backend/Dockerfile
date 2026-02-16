FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache wget && \
    addgroup -S spring && adduser -S spring -G spring

WORKDIR /app
COPY build/libs/*.jar app.jar
RUN chown -R spring:spring /app

USER spring:spring

ENTRYPOINT ["java", "-jar", "app.jar"]
