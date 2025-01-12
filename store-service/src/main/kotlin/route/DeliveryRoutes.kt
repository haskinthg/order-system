package ru.vgtu.storeservice.route

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.vgtu.storeservice.controller.DeliveryController

fun Application.configureRouting() {
    routing {
        route("/deliveries") {
            get {
                DeliveryController.getAllDeliveries(call)
            }
        }
        route("/deliveries/{delivery_id}/status") {
            post {
                DeliveryController.setStatus(call)
            }
        }
    }
}
