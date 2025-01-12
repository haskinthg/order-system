package ru.vgtu.paymentservice.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Payment(
    val id: Long = 0,
    val orderId: Long,
    val amount: Int,
    val status: String = "NEW"
)

object PaymentTable : Table("payments_service.payments") {
    val id = long("id")
    val orderId = long("order_id")
    val amount = integer("amount")
    val status = varchar("status", 50).default("NEW")
    override val primaryKey = PrimaryKey(id)
}
