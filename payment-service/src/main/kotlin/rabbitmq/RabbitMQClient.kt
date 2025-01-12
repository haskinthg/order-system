package ru.vgtu.paymentservice.rabbitmq

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DeliverCallback
import io.ktor.server.config.ApplicationConfig
import io.ktor.util.logging.*
import kotlinx.coroutines.runBlocking

object RabbitMQClient {
    private lateinit var config: ApplicationConfig
    private lateinit var connection: Connection
    private lateinit var channel: Channel
    private var isInitialized = false

    private val LOGGER = KtorSimpleLogger("ru.vgtu.paymentservice.rabbitmq.RabbitMQClient")

    const val ROUTING_KEY_NEW_ORDER: String = "new_order"
    const val ROUTING_KEY_PAYMENT_ORDER: String = "status_order"
    const val ROUTING_KEY_STORE_ORDER: String = "store_order"


    fun init(config: ApplicationConfig) {
        this.config = config
        val factory = ConnectionFactory().apply {
            host = config.property("rabbitmq.host").getString()
            port = config.property("rabbitmq.port").getString().toInt()
            config.propertyOrNull("rabbitmq.username")?.let { username = it.getString() }
            config.propertyOrNull("rabbitmq.password")?.let { password = it.getString() }
        }

        connection = factory.newConnection()
        channel = connection.createChannel()

        channel.queueDeclare(config.property("rabbitmq.queue").getString(), true, false, false, null)
        channel.queueBind(config.property("rabbitmq.queue").getString(), config.property("rabbitmq.exchange").getString(), ROUTING_KEY_NEW_ORDER)

        isInitialized = true
    }

    fun addConsumer(consumer: NewOrderConsumer) {
        val callback = DeliverCallback { _, delivery ->
            val message = String(delivery.body, Charsets.UTF_8)
            try {
                runBlocking {
                    consumer.handle(message)
                }
            } catch (e: Exception) {
                LOGGER.error(e)
            }
        }
        channel.basicConsume(config.property("rabbitmq.queue").getString(), true, callback) { consumerTag, _ ->
            LOGGER.info("Consumer cancelled: $consumerTag")
        }
    }

    fun sendMessage(message: String, routingKey: String) {
        if (!isInitialized) throw IllegalStateException("RabbitMQClient is not initialized")
        channel.basicPublish(
            config.property("rabbitmq.exchange").getString(),
            routingKey,
            null,
            message.toByteArray()
        )
    }

    fun close() {
        if (::channel.isInitialized) channel.close()
        if (::connection.isInitialized) connection.close()
    }
}
