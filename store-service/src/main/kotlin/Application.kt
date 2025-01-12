package ru.vgtu.storeservice

import io.ktor.server.application.*
import ru.vgtu.storeservice.route.configureRouting
import io.ktor.server.plugins.statuspages.StatusPages
import ru.vgtu.storeservice.config.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureFlyway()
    configureDatabase()
    configureSerialization()
    configureRouting()
    configureRabbitMQ()
    install(StatusPages)
}
