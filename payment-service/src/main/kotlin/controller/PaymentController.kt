package ru.vgtu.paymentservice.controller

import io.ktor.server.application.*
import io.ktor.server.response.respond

import io.ktor.http.HttpStatusCode
import ru.vgtu.paymentservice.model.Payment
import ru.vgtu.paymentservice.service.PaymentService

object PaymentController {

    suspend fun getAllPayments(call: ApplicationCall) {
        val orders: List<Payment> = PaymentService.getAllPayments()
        call.respond(HttpStatusCode.OK, orders)
    }

    suspend fun setStatus(call: ApplicationCall) {
        val paymentIdStr: String = call.parameters["payment_id"].toString()
        val paymentId: Long = paymentIdStr.toLong()
        val paymentStatus: String = call.request.queryParameters["payment_status"].toString()
        PaymentService.setStatus(paymentId, paymentStatus)
        call.respond(HttpStatusCode.OK, "Payment.kt status updated successfully")
    }
}
