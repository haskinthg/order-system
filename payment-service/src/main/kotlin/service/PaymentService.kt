package ru.vgtu.paymentservice.service

import io.ktor.server.config.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.vgtu.paymentservice.model.Payment
import ru.vgtu.paymentservice.model.PaymentTable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.vgtu.paymentservice.model.OrderStatusMessage
import ru.vgtu.paymentservice.model.StoreDeliveryMessage
import ru.vgtu.paymentservice.rabbitmq.RabbitMQClient

object PaymentService {
    private lateinit var config: ApplicationConfig

    fun init(config: ApplicationConfig) {
        this.config = config
    }

    fun getAllPayments(): List<Payment> {
        return transaction {
            PaymentTable.selectAll().map {
                Payment(
                    id = it[PaymentTable.id],
                    orderId = it[PaymentTable.orderId],
                    amount = it[PaymentTable.amount],
                    status = it[PaymentTable.status],
                )
            }
        }
    }

    fun getPaymentById(id: Long): Payment {
        return transaction {
            PaymentTable.selectAll().where { PaymentTable.id eq id }.limit(1).single().let {
                Payment(
                    id = it[PaymentTable.id],
                    orderId = it[PaymentTable.orderId],
                    amount = it[PaymentTable.amount],
                    status = it[PaymentTable.status],
                )
            }
        }
    }

    suspend fun addPayment(payment: Payment) {
        newSuspendedTransaction {
            PaymentTable.insert {
                it[orderId] = payment.orderId
                it[amount] = payment.amount
                it[status] = payment.status
            }
            updateOrderStatus(payment.orderId, "PAYMENT.NEW")
        }
    }

    suspend fun setStatus(paymentId: Long, paymentStatus: String) {
        newSuspendedTransaction {
            PaymentTable.update({ PaymentTable.id eq paymentId }) {
                it[status] = paymentStatus
            }

            if (paymentStatus == "DONE") {
                val orderId = getPaymentById(paymentId).orderId
                updateOrderStatus(orderId, "PAYMENT.DONE")
                toStore(getPaymentById(paymentId))
            }

            if (paymentStatus == "CANCEL") {
                val orderId = getPaymentById(paymentId).orderId
                updateOrderStatus(orderId, "PAYMENT.CANCEL")
            }
        }
    }

    private fun updateOrderStatus(orderId: Long, orderStatus: String) {
        val message = OrderStatusMessage(orderId, orderStatus)
        RabbitMQClient.sendMessage(Json.encodeToString(message), RabbitMQClient.ROUTING_KEY_PAYMENT_ORDER)
    }

    private fun toStore(payment: Payment) {
        val message = StoreDeliveryMessage(payment.id, payment.orderId)
        val routingKey = "${StoreMockService.getStore()}_${RabbitMQClient.ROUTING_KEY_STORE_ORDER}"
        RabbitMQClient.sendMessage(Json.encodeToString(message), routingKey)
    }
}
