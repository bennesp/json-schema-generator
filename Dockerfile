# Dockerfile for a kotlin ktor server project
FROM gradle:7.6.0-jdk17 as builder
WORKDIR /home/gradle/src
COPY . .
RUN gradle build
RUN ls -lha /home/gradle/src/build/libs/

FROM amazoncorretto:17.0.5
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
