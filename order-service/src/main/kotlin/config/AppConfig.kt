package ru.vgtu.orderservice.config

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.sql.Database
import org.flywaydb.core.Flyway
import ru.vgtu.orderservice.rabbitmq.PaymentOrderConsumer
import ru.vgtu.orderservice.rabbitmq.RabbitMQClient

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
        .defaultSchema("orders_service")
        .createSchemas(true)
        .table("order_flyway_schema_history")
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
    RabbitMQClient.addConsumer(PaymentOrderConsumer)
    environment.monitor.subscribe(ApplicationStopped) {
        RabbitMQClient.close()
    }
}
