package ru.vgtu.paymentservice.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderStatusMessage(
    val orderId: Long,
    val status: String
)
