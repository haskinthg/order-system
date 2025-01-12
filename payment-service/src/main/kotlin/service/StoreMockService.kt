package ru.vgtu.paymentservice.service

object StoreMockService {
    private val stores = listOf("voronezh36", "tula71", "moscow77");

    fun getStore(): String {
        return stores.random()
    }
}