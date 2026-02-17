package com.range.killerbot.checker

import com.range.killerbot.properties.BotProperties
import jakarta.annotation.PostConstruct
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ServerChecker(
private val botProperties: BotProperties
) : ListenerAdapter() {



    private val logger: Logger = LoggerFactory.getLogger(ServerChecker::class.java)



    override fun onGuildJoin(event: GuildJoinEvent) {
        val guild = event.guild
        if (guild.id != botProperties.serverId) {
            logger.info("Not allowed server added: ${guild.name}. Leaving...")
            guild.leave().queue()
        }
    }
}
