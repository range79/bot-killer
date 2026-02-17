package com.range.killerbot.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("message")
data class MessageProperties (
    var messageSpamLimit: Long,
    var messageInterval: Long,
    var muteDurationMinutes: Long,
)