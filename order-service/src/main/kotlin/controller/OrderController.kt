package ru.vgtu.orderservice.controller

import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.request.receive

import io.ktor.http.HttpStatusCode

import ru.vgtu.orderservice.service.OrderService
import ru.vgtu.orderservice.model.Order

object OrderController {

    suspend fun getAllOrders(call: ApplicationCall) {
        val orders: List<Order> = OrderService.getAllOrders()
        call.respond(HttpStatusCode.OK, orders)
    }

    suspend fun createOrder(call: ApplicationCall) {
        val order: Order = call.receive<Order>()
        OrderService.createOrder(order)
        call.respond(HttpStatusCode.Created, "Order created successfully")
    }

    suspend fun setStatus(call: ApplicationCall) {
        val orderIdStr: String = call.parameters["order_id"].toString()
        val orderId: Long = orderIdStr.toLong()
        val orderStatus: String = call.request.queryParameters["order_status"].toString()
        OrderService.setStatus(orderId, orderStatus)
        call.respond(HttpStatusCode.OK, "Order status updated successfully")
    }
}
