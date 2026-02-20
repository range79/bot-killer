package com.range.killerbot.checker

import com.range.killerbot.properties.MessageProperties
import com.range.killerbot.service.MessageSaveService
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class MessageChecker(
    private val messageProperties: MessageProperties,
    private val messageSaveService: MessageSaveService,
) : ListenerAdapter() {

    private val log = LoggerFactory.getLogger(MessageChecker::class.java)

    private val urlRegex =
        """https?://[^\s/$.?#].\S*""".toRegex(RegexOption.IGNORE_CASE)

    override fun onMessageReceived(event: MessageReceivedEvent) {

        log.info(
            "EVENT msgId={} user={} guild={}",
            event.messageId,
            event.author.id,
            event.guild.id
        )

        if (event.author.isBot) return
        if (!event.isFromGuild) return

        val userId = event.author.id

        val member = event.member ?: run {
            log.warn("MEMBER NULL user={} guild={}", userId, event.guild.id)
            return
        }

        val msg = event.message

        val isImage = msg.attachments.any { it.isImage }
        val hasUrl = urlRegex.containsMatchIn(msg.contentRaw)
        val isMedia = isImage || hasUrl

        log.info("MEDIA image={} url={} result={}", isImage, hasUrl, isMedia)

        if (!isMedia) return

        log.info("CONTENT {}", msg.contentRaw.take(150))

        try {
            messageSaveService.save(msg.contentRaw, userId)
            log.info("REDIS SAVE OK user={}", userId)
        } catch (e: Exception) {
            log.error("REDIS SAVE FAIL", e)
        }

        val count = try {
            messageSaveService.countUserId(userId)
        } catch (e: Exception) {
            log.error("REDIS COUNT FAIL", e)
            -1
        }

        log.info(
            "COUNT user={} count={} limit={}",
            userId,
            count,
            messageProperties.messageSpamLimit
        )

        if (count < messageProperties.messageSpamLimit) return

        log.warn(
            "SPAM user={} tag={} count={}",
            userId,
            member.user.asTag,
            count
        )

        member.timeoutFor(
            Duration.ofMinutes(messageProperties.muteDurationMinutes)
        ).queue(
            {
                log.info("TIMEOUT OK {}", member.user.asTag)
            },
            { err ->
                log.error("TIMEOUT FAIL {}", err.message)
            }
        )

        val limit = messageProperties.imageDeleteLimit.coerceIn(1, 100)

        deleteUserMediaMessagesInAllTextChannels(
            event.guild,
            userId,
            limit
        )

        try {
            messageSaveService.deleteUserMessages(userId)
            log.info("REDIS DELETE OK user={}", userId)
        } catch (e: Exception) {
            log.error("REDIS DELETE FAIL", e)
        }

        logUserMuted(event.guild, member)
    }

    private fun deleteUserMediaMessagesInAllTextChannels(
        guild: Guild,
        userId: String,
        limit: Int
    ) {

        guild.textChannels.forEach { ch ->

            log.info("SCAN CHANNEL #{}", ch.name)

            deleteRecentUserMediaMessages(ch, userId, limit)
        }
    }

    private fun deleteRecentUserMediaMessages(
        channel: TextChannel,
        userId: String,
        limit: Int
    ) {

        channel.iterableHistory
            .takeAsync(limit)
            .thenAccept { messages ->

                log.info(
                    "HISTORY #{} size={}",
                    channel.name,
                    messages.size
                )

                messages.asSequence()
                    .filter { it.author.id == userId }
                    .filter {
                        it.attachments.any { a -> a.isImage } ||
                                urlRegex.containsMatchIn(it.contentRaw)
                    }
                    .forEach { it ->

                        it.delete().queue(
                            {
                                log.info("DELETE OK {}",it )
                            },
                            { err ->
                                log.error(
                                    "DELETE FAIL {} {}",
                                    it.id,
                                    err.message
                                )
                            }
                        )
                    }
            }
            .exceptionally { err ->
                log.error("HISTORY FAIL", err)
                null
            }
    }

    private fun logUserMuted(guild: Guild, member: Member) {

        val logChannelId = messageProperties.logChannelId

        if (logChannelId.isNullOrBlank()) return

        val logChannel =
            guild.getTextChannelById(logChannelId) ?: return

        val text = """
User timed out for media spam
User: ${member.user.asTag}
Duration: ${messageProperties.muteDurationMinutes} minutes
""".trimIndent()

        logChannel.sendMessage(text).queue(
            {
                log.info("LOG SENT")
            },
            { err ->
                log.error("LOG FAIL {}", err.message)
            }
        )
    }
}