package com.range.killerbot.properties

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("killerbot")
data class BotProperties (
    var token: String? = null,
    var serverId: String?= null,
){
    @PostConstruct
    fun onPostConstruct(){
        require(!token.isNullOrEmpty()) {
            "BOT_TOKEN is missing. Please set it in .env file."
        }

        require(!serverId.isNullOrEmpty()) {
            "SERVER_ID is missing. Please set it in .env file."
        }

        }
    }
