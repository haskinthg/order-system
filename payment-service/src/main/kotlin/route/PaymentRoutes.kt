package ru.vgtu.paymentservice.route

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.vgtu.paymentservice.controller.PaymentController

fun Application.configureRouting() {
    routing {
        route("/payments") {
            get {
                PaymentController.getAllPayments(call)
            }
        }
        route("/payments/{payment_id}/status") {
            post {
                PaymentController.setStatus(call)
            }
        }
    }
}
