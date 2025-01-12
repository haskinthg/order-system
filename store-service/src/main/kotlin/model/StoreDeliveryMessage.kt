package ru.vgtu.storeservice.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreDeliveryMessage(
    val paymentId: Long,
    val orderId: Long
)
