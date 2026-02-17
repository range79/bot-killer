package com.range.killerbot.properties

import com.range.killerbot.exception.ServerIDISEmptyORNullException
import com.range.killerbot.exception.TokenIsEmptyOrNullException
import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("killerbot")
data class BotProperties (
    var token: String? = null,
    var serverId: String?= null,
){
    @PostConstruct
    fun onPostConstruct(){
        if (token.isNullOrBlank()) {
            throw TokenIsEmptyOrNullException("DiscordBot token cannot be null")
        }
        if (serverId.isNullOrBlank()) {
            throw ServerIDISEmptyORNullException("DiscordBot serverId cannot be null or Empty")
        }
    }
}