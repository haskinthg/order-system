package ru.vgtu.orderservice.route

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.vgtu.orderservice.controller.OrderController

fun Application.configureRouting() {
    routing {
        route("/orders") {
            get {
                OrderController.getAllOrders(call)
            }
            post {
                OrderController.createOrder(call)
            }
        }
        route("/orders/{order_id}/status") {
            post {
                OrderController.setStatus(call)
            }
        }
    }
}
