package com.range.killerbot.domain.repository

import com.range.killerbot.domain.entity.MessageModel
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<MessageModel, String> {

    fun countByAuthor(author: String): Long
    fun deleteAllByAuthor(author: String)
}