package ru.vgtu.orderservice.service

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import ru.vgtu.orderservice.model.Order
import ru.vgtu.orderservice.model.OrderTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.vgtu.orderservice.model.OrderStatusMessage
import ru.vgtu.orderservice.rabbitmq.RabbitMQClient

object OrderService {

    fun getAllOrders(): List<Order> {
        return transaction {
            OrderTable.selectAll().map {
                Order(
                    id = it[OrderTable.id],
                    customer = it[OrderTable.customer],
                    product = it[OrderTable.product],
                    quantity = it[OrderTable.quantity],
                    status = it[OrderTable.status],
                )
            }
        }
    }

    fun getOrderById(orderId: Long): Order {
        return transaction {
            OrderTable.selectAll().where { OrderTable.id eq orderId }.map {
                Order(
                    id = it[OrderTable.id],
                    customer = it[OrderTable.customer],
                    product = it[OrderTable.product],
                    quantity = it[OrderTable.quantity],
                    status = it[OrderTable.status],
                )
            }.first()
        }
    }

    fun createOrder(order: Order) {
        transaction {
            val id = OrderTable.insertReturning {
                it[customer] = order.customer
                it[product] = order.product
                it[quantity] = order.quantity
                it[status] = order.status
            }.first()[OrderTable.id]

            val newOrder = getOrderById(id)
            val message = OrderStatusMessage(newOrder.id, newOrder.status)
            RabbitMQClient.sendMessage(Json.encodeToString(message), RabbitMQClient.ROUTING_KEY_NEW_ORDER)
        }
    }

    fun setStatus(orderId: Long, orderStatus: String) {
        transaction {
            OrderTable.update({ OrderTable.id eq orderId }) {
                it[status] = orderStatus
            }
        }
    }
}
