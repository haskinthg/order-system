package ru.vgtu.orderservice.rabbitmq

import io.ktor.util.logging.*
import kotlinx.serialization.json.Json
import ru.vgtu.orderservice.model.OrderStatusMessage
import ru.vgtu.orderservice.service.OrderService

object PaymentOrderConsumer: Consumer {

    private val LOGGER = KtorSimpleLogger(this.javaClass.canonicalName)

    override fun handle(message: String) {
        LOGGER.info("New message received: $message")
        val order: OrderStatusMessage = Json.decodeFromString<OrderStatusMessage>(message)
        OrderService.setStatus(order.orderId, order.status)
    }
}