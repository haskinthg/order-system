package ru.vgtu.storeservice.rabbitmq

import io.ktor.util.logging.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import ru.vgtu.storeservice.model.Delivery
import ru.vgtu.storeservice.model.StoreDeliveryMessage
import ru.vgtu.storeservice.service.DeliveryService

object DeliveryConsumer: Consumer {

    private val LOGGER = KtorSimpleLogger(this.javaClass.canonicalName)

    override fun handle(message: String) {
        LOGGER.info("New message received: $message")
        val order: StoreDeliveryMessage = Json.decodeFromString<StoreDeliveryMessage>(message)
        val delivery = Delivery (
            0,
            order.orderId,
            order.paymentId
        )
        runBlocking {
            DeliveryService.addDelivery(delivery)
        }
    }
}