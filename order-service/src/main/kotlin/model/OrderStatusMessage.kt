package ru.vgtu.orderservice.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderStatusMessage(
    val orderId: Long,
    val status: String
)
