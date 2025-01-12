package ru.vgtu.storeservice.rabbitmq

interface Consumer {
    fun handle(message: String)
}