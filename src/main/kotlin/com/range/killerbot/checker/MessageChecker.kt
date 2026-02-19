package com.range.killerbot.checker

import com.range.killerbot.properties.MessageProperties
import com.range.killerbot.service.MessageSaveService
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class MessageChecker(
    private val messageProperties: MessageProperties,
    private val messageSaveService: MessageSaveService,
) : ListenerAdapter() {

    private val log = LoggerFactory.getLogger(MessageChecker::class.java)
    private val urlRegex = Regex("""https?://""")

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return
        if (!event.isFromGuild) return

        val userId = event.author.id
        val member = event.member ?: return
        val msg = event.message

        val isMedia = msg.attachments.any { it.isImage } || urlRegex.containsMatchIn(msg.contentRaw)
        if (!isMedia) return

        val count = messageSaveService.countUserId(userId)

        if (count >= messageProperties.messageSpamLimit) {
            log.warn("User {} media spam count={}", member.user.asTag, count)

            member.timeoutFor(messageProperties.muteDurationMinutes, TimeUnit.MINUTES).queue()

            deleteUserMediaMessagesInAllTextChannels(event.guild, userId)

            messageSaveService.deleteUserMessages(userId)
            return
        }

        messageSaveService.save(msg.contentRaw, userId)
    }

    private fun deleteUserMediaMessagesInAllTextChannels(guild: Guild, userId: String) {
        guild.textChannels.forEach { ch ->

            deleteRecentUserMediaMessages(ch, userId, messageProperties.imageDeletedLimit?:50)
        TODO("fix app properties")
        }
    }

    private fun deleteRecentUserMediaMessages(channel: TextChannel, userId: String, limit: Int) {
        channel.iterableHistory.takeAsync(limit).thenAccept { messages ->
            messages.asSequence()
                .filter { it.author.id == userId }
                .filter { it.attachments.any { a -> a.isImage } || urlRegex.containsMatchIn(it.contentRaw) }
                .forEach { it.delete().queue() }
        }
    }
}
