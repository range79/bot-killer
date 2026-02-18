package com.range.killerbot.config

import com.range.killerbot.checker.MessageChecker
import com.range.killerbot.checker.ServerChecker
import com.range.killerbot.properties.BotProperties
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class JDAConfig(

    private var messageChecker: MessageChecker,
    private var serverChecker: ServerChecker,
    private var botProperties: BotProperties
){


    private val logger = LoggerFactory.getLogger(JDAConfig::class.java)


    @Bean
    fun run(): JDA {
        val jda = JDABuilder.createDefault(botProperties.token,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.MESSAGE_CONTENT).build()
        jda.addEventListener(messageChecker, serverChecker)
        jda.awaitReady()
        jda.guilds.forEach { guild ->
            if (guild.id != botProperties.serverId) {
                logger.info("Not allowed server at startup: ${guild.name}. Leaving...")
                guild.leave().queue()
            }

        }

        return  jda

    }
}