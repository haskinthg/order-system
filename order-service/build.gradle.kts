val ktorVersion: String by project
val exposedVersion: String by project

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("io.ktor.plugin") version "3.0.3"
}

group = "ru.vgtu.orderservice"
version = "1.0"


application {
    mainClass.set("ru.vgtu.orderservice.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation ("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation ("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation ("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation ("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation ("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation ("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation ("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation ("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    implementation ("org.flywaydb:flyway-core:9.10.0")
    implementation ("org.postgresql:postgresql:42.2.5")

    implementation ("io.ktor:ktor-server-cio:$ktorVersion")
    implementation ("io.ktor:ktor-server-html-builder:$ktorVersion")
    implementation ("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation ("io.ktor:ktor-server-call-logging:$ktorVersion")

    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    implementation("com.rabbitmq:amqp-client:5.15.0")
}

kotlin {
    jvmToolchain(21)
}