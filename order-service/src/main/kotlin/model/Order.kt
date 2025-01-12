package ru.vgtu.orderservice.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Order(
    val id: Long = 0,
    val customer: String,
    val product: String,
    val quantity: Int,
    val status: String = "NEW"
)

object OrderTable : Table("orders_service.orders") {
    val id = long("id")
    val customer = varchar("customer", 255)
    val product = varchar("product", 255)
    val quantity = integer("quantity")
    val status = varchar("status", 50).default("NEW")
    override val primaryKey = PrimaryKey(id)
}
