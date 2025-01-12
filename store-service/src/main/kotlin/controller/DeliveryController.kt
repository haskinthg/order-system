package ru.vgtu.storeservice.controller

import io.ktor.server.application.*
import io.ktor.server.response.respond

import io.ktor.http.HttpStatusCode
import ru.vgtu.storeservice.service.DeliveryService

object DeliveryController {

    suspend fun getAllDeliveries(call: ApplicationCall) {
        val deliveries = DeliveryService.getAllDeliveries()
        call.respond(deliveries)
    }

    suspend fun setStatus(call: ApplicationCall) {
        val deliveryIdStr: String = call.parameters["delivery_id"].toString()
        val deliveryId: Long = deliveryIdStr.toLong()
        val storeStatus: String = call.request.queryParameters["delivery_status"].toString()
        DeliveryService.setStatus(deliveryId, storeStatus)
        call.respond(HttpStatusCode.OK, "Delivery.kt status updated successfully")
    }
}
