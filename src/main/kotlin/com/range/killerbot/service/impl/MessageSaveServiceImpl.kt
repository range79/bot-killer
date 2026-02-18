package com.range.killerbot.service.impl

import com.range.killerbot.domain.entity.MessageModel
import com.range.killerbot.repository.MessageRepository
import com.range.killerbot.service.MessageSaveService
import org.springframework.stereotype.Service

@Service
class MessageSaveServiceImpl (
    private val messageRepository: MessageRepository,
): MessageSaveService {
    override fun save(message: MessageModel): MessageModel? {
        return messageRepository.save(message)
    }

    override fun countUserId(author: String): Int {
        return messageRepository.countMessageByAuthor(author)
    }
}