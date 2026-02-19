package com.range.killerbot.service.impl

import com.range.killerbot.domain.entity.MessageModel
import com.range.killerbot.domain.repository.MessageRepository
import com.range.killerbot.properties.MessageProperties
import com.range.killerbot.service.MessageSaveService
import org.springframework.stereotype.Service
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Service
class MessageSaveServiceImpl (
    private val messageRepository: MessageRepository,
    private val messageProperties: MessageProperties
): MessageSaveService {
    @OptIn(ExperimentalUuidApi::class)
    override fun save(message: String,author: String): MessageModel {
        val messageModel = MessageModel(
            Uuid.generateV7(),
            message,
            author,
            messageProperties.messageInterval
        )
        return messageRepository.save(messageModel)
    }

    override fun countUserId(author: String): Int {
        return messageRepository.countMessageByAuthor(author)
    }

    override fun deleteUserMessages(author: String) {
    return messageRepository.deleteAllByAuthor(author)
    }


}