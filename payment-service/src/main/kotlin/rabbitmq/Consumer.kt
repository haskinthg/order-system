package ru.vgtu.paymentservice.rabbitmq

interface Consumer {
    fun handle(message: String)
}