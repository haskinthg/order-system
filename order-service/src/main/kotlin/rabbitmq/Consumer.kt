package ru.vgtu.orderservice.rabbitmq

interface Consumer {
    fun handle(message: String)
}