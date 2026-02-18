package com.range.killerbot.repository

import com.range.killerbot.domain.entity.MessageModel
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<MessageModel, String> {
    fun countMessageByAuthor(author: String): Int
}