package com.range.killerbot.service

import com.range.killerbot.domain.entity.MessageModel

interface MessageSaveService {
    fun save(message: String,author: String): MessageModel
    fun countUserId(author: String): Long
    fun deleteUserMessages(author: String)
}