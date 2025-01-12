package ru.vgtu.storeservice.service

import io.ktor.server.config.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.vgtu.storeservice.model.Delivery
import ru.vgtu.storeservice.model.DeliveryTable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.vgtu.storeservice.model.OrderStatusMessage
import ru.vgtu.storeservice.model.StoreDeliveryMessage
import ru.vgtu.storeservice.rabbitmq.RabbitMQClient

object DeliveryService {
    private lateinit var config: ApplicationConfig

    fun init(config: ApplicationConfig) {
        this.config = config
    }

    fun getAllDeliveries(): List<Delivery> {
        return transaction {
            DeliveryTable.selectAll().map {
                Delivery(
                    id = it[DeliveryTable.id],
                    orderId = it[DeliveryTable.orderId],
                    paymentId = it[DeliveryTable.paymentId],
                    status = it[DeliveryTable.status],
                )
            }
        }
    }

    fun getDeliveryById(id: Long): Delivery {
        return transaction {
            DeliveryTable.selectAll().where { DeliveryTable.id eq id }.limit(1).single().let {
                Delivery(
                    id = it[DeliveryTable.id],
                    orderId = it[DeliveryTable.orderId],
                    paymentId = it[DeliveryTable.paymentId],
                    status = it[DeliveryTable.status],
                )
            }
        }
    }

    suspend fun addDelivery(delivery: Delivery) {
        newSuspendedTransaction {
            DeliveryTable.insert {
                it[orderId] = delivery.orderId
                it[paymentId] = delivery.paymentId
                it[status] = delivery.status
            }
            updateOrderStatus(delivery.orderId, "DELIVERY.NEW")
        }
    }

    suspend fun setStatus(deliveryId: Long, deliveryStatus: String) {
        newSuspendedTransaction {
            DeliveryTable.update({ DeliveryTable.id eq deliveryId }) {
                it[status] = deliveryStatus
            }

            if (deliveryStatus == "DONE") {
                val orderId = getDeliveryById(deliveryId).orderId
                updateOrderStatus(orderId, "DELIVERY.DONE")
            }

            if (deliveryStatus == "CANCEL") {
                val orderId = getDeliveryById(deliveryId).orderId
                updateOrderStatus(orderId, "DELIVERY.CANCEL")
            }
        }
    }

    private fun updateOrderStatus(orderId: Long, orderStatus: String) {
        val message = OrderStatusMessage(orderId, orderStatus)
        RabbitMQClient.sendMessage(Json.encodeToString(message), RabbitMQClient.ROUTING_KEY_STATUS_ORDER)
    }
}
