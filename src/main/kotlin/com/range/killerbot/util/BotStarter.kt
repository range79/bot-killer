package com.range.killerbot.util

import com.range.killerbot.checker.MessageChecker
import com.range.killerbot.checker.ServerChecker
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class BotStarter(

    private var messageChecker: MessageChecker,
    private var serverChecker: ServerChecker
){   @Value("\${discord.serverid}")
private lateinit var allowedServerID: String

    private val logger = LoggerFactory.getLogger(BotStarter::class.java)
    @Value("\${killerbot.botToken}")
    private lateinit var token: String

    @Bean
    fun run(): JDA {
        val jda = JDABuilder.createDefault(token,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.MESSAGE_CONTENT).build()
        jda.addEventListener(messageChecker, serverChecker)




        jda.awaitReady()
        jda.guilds.forEach { guild ->
            if (guild.id != allowedServerID) {
                logger.info("Not allowed server at startup: ${guild.name}. Leaving...")
                guild.leave().queue()
            }

        }

        return  jda

    }
}