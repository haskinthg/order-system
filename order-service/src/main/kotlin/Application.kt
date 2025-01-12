package ru.vgtu.orderservice

import io.ktor.server.application.*
import ru.vgtu.orderservice.route.configureRouting
import ru.vgtu.orderservice.config.configureDatabase
import ru.vgtu.orderservice.config.configureFlyway
import io.ktor.server.plugins.statuspages.StatusPages
import ru.vgtu.orderservice.config.configureRabbitMQ
import ru.vgtu.orderservice.config.configureSerialization

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureFlyway()
    configureDatabase()
    configureSerialization()
    configureRouting()
    configureRabbitMQ()

    install(StatusPages)
}
