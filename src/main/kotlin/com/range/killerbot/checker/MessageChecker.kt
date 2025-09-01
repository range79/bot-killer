package com.range.killerbot.checker


import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class MessageChecker(
    private val redisTemplate: RedisTemplate<String, String>
) : ListenerAdapter() {
    @Value("\${discord.log.channel-id}")
    private lateinit var logChannelId: String

    private val logger: Logger = LoggerFactory.getLogger(MessageChecker::class.java)

    private val SPAM_LIMIT = 8
    private val TIME_WINDOW_SECONDS = 30L
    private val MUTE_DURATION_HOURS = 10L

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val userId = event.author.id
        val message = event.message
        val content = message.contentRaw
        val attachments = message.attachments

        val isMedia = attachments.any { it.isImage } || content.contains("http")

        if (!isMedia) return
        val key = "spam_multi:$userId"

        val current = redisTemplate.opsForValue().get(key)?.toIntOrNull() ?: 0
        val newCount = current + 1
        logger.info("current: $current, $newCount")

        redisTemplate.opsForValue().set(key, newCount.toString(), Duration.ofSeconds(TIME_WINDOW_SECONDS))
        logger.error("redis: ${redisTemplate.opsForValue().get(key)}")

        if (newCount >= SPAM_LIMIT) {
            val guild = event.guild
            val member = event.getMember()
            logger.info("$member")

            if (member != null && guild.selfMember.canInteract(member)) {
                logger.info("${guild.selfMember.canInteract(member)}")
                val logChannel = guild.getTextChannelById(logChannelId)
                logger.info("$guild, ${guild.selfMember.asMention},${logChannel}")
                logChannel!!.sendMessage("User ${event.author.name} spammed images and muted!").queue()

                member.timeoutFor(Duration.ofHours(MUTE_DURATION_HOURS)).queue()
                logger.info("User ${event.author.name} muted for $MUTE_DURATION_HOURS hours due to spam")


                guild.textChannels.forEach { channel ->
                    deleteUserMediaMessages(channel, userId)
                }
            }
            redisTemplate.delete(key)
        }
    }

    private fun deleteUserMediaMessages(channel: TextChannel, userId: String) {
        Thread.sleep(1000)
        channel.iterableHistory.takeAsync(1000).thenAccept { messages ->
            messages.filter { it.author.id == userId && (it.attachments.any { a -> a.isImage } || it.contentRaw.contains("http")) }
                .forEach { it.delete().queue() }
        }
    }
}

