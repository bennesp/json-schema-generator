FROM eclipse-temurin:21.0.3_9-jdk as builder
WORKDIR /home/gradle/src

# Download dependencies
COPY build.gradle.kts gradle.properties settings.gradle.kts gradlew .
COPY gradle ./gradle
RUN ./gradlew build

# Build application
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:21.0.3_9-jre
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
