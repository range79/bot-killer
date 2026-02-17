package com.range.killerbot.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("killerbot")
data class BotProperties (
    var token: String? = null,
    var serverId: String?= null,
)