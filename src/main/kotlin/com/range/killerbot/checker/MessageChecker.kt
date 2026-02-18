package com.range.killerbot.checker


import com.range.killerbot.domain.repository.MessageRepository
import com.range.killerbot.exception.LogChannelNotFoundException
import com.range.killerbot.properties.MessageProperties
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class MessageChecker(
    private val messageRepository: MessageRepository,
    private val messageProperties: MessageProperties,
    ) : ListenerAdapter() {


    private val logger: Logger = LoggerFactory.getLogger(MessageChecker::class.java)


    override fun onMessageReceived(event: MessageReceivedEvent) {

        val userId = event.author.id
        val message = event.message
        val content = message.contentRaw
        val attachments = message.attachments

        val isMedia = attachments.any { it.isImage } || content.contains("http")
        if (event.author.isBot) return
        if (!isMedia) return
9


        }


    private fun deleteUserMediaMessages(channel: TextChannel, userId: String) {
        Thread.sleep(1000)
        channel.iterableHistory.takeAsync(1000).thenAccept { messages ->
            messages.filter {
                it.author.id == userId && (it.attachments.any { a -> a.isImage } || it.contentRaw.contains(
                    "http"
                ))
            }
                .forEach { it.delete().queue() }
        }
    }

    private fun deleteUserMediaMessagesInVoiceChannel(voiceChannel: AudioChannelUnion, userId: String) {
        Thread.sleep(1000)
        val messageChannel = voiceChannel.asGuildMessageChannel()

        messageChannel.iterableHistory.takeAsync(1000).thenAcceptAsync { messages ->
            messages.filter {
                it.author.id == userId && (it.attachments.any { a -> a.isImage } || it.contentRaw.contains(
                    "http"
                ))
            }
                .forEach { it.delete().queue() }
        }
    }

}

