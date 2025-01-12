package ru.vgtu.paymentservice.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreDeliveryMessage(
    val paymentId: Long,
    val orderId: Long
)
