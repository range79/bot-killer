package com.range.killerbot.properties

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("message")
data class MessageProperties (
    var messageSpamLimit: Long,
    var messageInterval: Long,
    var muteDurationMinutes: Long,
    var logChannelId: String?,
){
    companion object{
        private val logger = LoggerFactory.getLogger(MessageProperties::class.java)
    }
    @PostConstruct
    fun onPostConstruct(){
        if (logChannelId.isNullOrBlank() ) {
            logger.warn("Message channel id cannot be set explicitly")
        }
    }
}