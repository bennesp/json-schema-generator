import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "2.1.21"
}

group = "it.bennesp.json-schema-generator"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    @Suppress("LocalVariableName")
    val ktor_version = "3.1.3"

    @Suppress("LocalVariableName")
    val logback_version = "1.5.18"

    // Ktor server
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-compression:$ktor_version")
    implementation("io.ktor:ktor-server-call-id:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")

    // Ktor client
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    // logback
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    implementation("ch.qos.logback.contrib:logback-jackson:0.1.5")

    // json-schema-inferrer and its dependencies with non-vulnerable versions
    implementation("com.github.saasquatch:json-schema-inferrer:0.2.1")
    implementation("org.yaml:snakeyaml:2.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.19.0")

    // Cx78f40514-81ff: the vulnerable function is not used
    implementation("commons-validator:commons-validator:1.9.0")

    // Force transient dependencies to use a newer version to avoid vulnerabilities in ktor-server-netty
    implementation("io.netty:netty-codec:4.2.1.Final")

    testImplementation(kotlin("test"))
}

configurations.all {
    // Avoid Cx78f40514-81ff replacing old commons-collections with commons-collections4
    resolutionStrategy.dependencySubstitution {
        substitute(module("commons-collections:commons-collections:3.2.2"))
            .using(module("org.apache.commons:commons-collections4:4.5.0"))
    }
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 4

    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "it.bennes.jsonSchemaGenerator.MainKt"
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
