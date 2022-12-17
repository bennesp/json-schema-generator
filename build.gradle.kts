import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
}

group = "it.bennesp.json-schema-generator"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    @Suppress("LocalVariableName")
    val ktor_version = "2.2.1"
    @Suppress("LocalVariableName")
    val logback_version = "1.4.5"

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

    implementation("com.github.saasquatch:json-schema-inferrer:0.1.5")

    // CVE-2022-1471: https://github.com/FasterXML/jackson-dataformats-text/issues/361
    // CVE-2022-41854: ???
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.1")

    // Cx78f40514-81ff: the vulnerable function is not used
    @Suppress("VulnerableLibrariesLocal")
    implementation("commons-validator:commons-validator:1.7")

    // Force transient dependencies to use a newer version to avoid vulnerabilities in ktor-server-netty
    implementation("io.netty:netty-codec:4.1.86.Final")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
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
