package ru.vgtu.paymentservice

import io.ktor.server.application.*
import ru.vgtu.paymentservice.route.configureRouting
import io.ktor.server.plugins.statuspages.StatusPages
import ru.vgtu.paymentservice.config.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureFlyway()
    configureDatabase()
    configureSerialization()
    configureRouting()
    configureRabbitMQ()
    install(StatusPages)
}
