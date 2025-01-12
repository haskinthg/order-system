package ru.vgtu.paymentservice.config

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.sql.Database
import org.flywaydb.core.Flyway
import ru.vgtu.paymentservice.rabbitmq.NewOrderConsumer
import ru.vgtu.paymentservice.rabbitmq.RabbitMQClient
import ru.vgtu.paymentservice.service.PaymentService

fun Application.configureDatabase() {
    val dbUrl = environment.config.property("ktor.database.url").getString()
    val dbDriver = environment.config.property("ktor.database.driver").getString()
    val dbUser = environment.config.property("ktor.database.user").getString()
    val dbPassword = environment.config.property("ktor.database.password").getString()

    Database.connect(
        url = dbUrl,
        driver = dbDriver,
        user = dbUser,
        password = dbPassword
    )
}

fun Application.configureFlyway() {
    val dbUrl = environment.config.property("ktor.database.url").getString()
    val dbUser = environment.config.property("ktor.database.user").getString()
    val dbPassword = environment.config.property("ktor.database.password").getString()

    Flyway.configure()
        .dataSource(dbUrl, dbUser, dbPassword)
        .defaultSchema("payments_service")
        .createSchemas(true)
        .table("payment_flyway_schema_history")
        .load()
        .migrate()
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

fun Application.configureRabbitMQ() {
    RabbitMQClient.init(environment.config)
    RabbitMQClient.addConsumer(NewOrderConsumer)
    environment.monitor.subscribe(ApplicationStopped) {
        RabbitMQClient.close()
    }
}

fun Application.configureServices() {
    PaymentService.init(environment.config)
}
