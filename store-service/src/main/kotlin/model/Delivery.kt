package ru.vgtu.storeservice.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Delivery(
    val id: Long = 0,
    val orderId: Long,
    val paymentId: Long,
    val status: String = "NEW"
)

object DeliveryTable : Table("store_service_${System.getenv("STORE_NAME")}.deliveries") {
    val id = long("id")
    val orderId = long("order_id")
    val paymentId = long("payment_id")
    val status = varchar("status", 50).default("NEW")
    override val primaryKey = PrimaryKey(id)
}
