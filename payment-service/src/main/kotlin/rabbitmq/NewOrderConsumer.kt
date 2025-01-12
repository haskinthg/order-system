package ru.vgtu.paymentservice.rabbitmq

import io.ktor.util.logging.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import ru.vgtu.paymentservice.model.OrderStatusMessage
import ru.vgtu.paymentservice.model.Payment
import ru.vgtu.paymentservice.service.PaymentService
import kotlin.random.Random

object NewOrderConsumer: Consumer {

    private val LOGGER = KtorSimpleLogger(this.javaClass.canonicalName)

    override fun handle(message: String) {
        LOGGER.info("New message received: $message")
        val order: OrderStatusMessage = Json.decodeFromString<OrderStatusMessage>(message)
        val payment = Payment (
            0,
            order.orderId,
            Random.nextInt(1000, 10000),
        )
        runBlocking {
            PaymentService.addPayment(payment)
        }
    }
}