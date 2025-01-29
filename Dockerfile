ARG JVM_VERSION=21.0.5_11

FROM eclipse-temurin:${JVM_VERSION}-jdk AS builder
WORKDIR /home/gradle/src

# Download dependencies
COPY build.gradle.kts gradle.properties settings.gradle.kts gradlew ./
COPY gradle ./gradle
RUN ./gradlew build

# Build application
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:${JVM_VERSION}-jre
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
