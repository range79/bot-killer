package com.range.killerbot.service

import com.range.killerbot.domain.entity.MessageModel
import net.dv8tion.jda.api.entities.Message

interface MessageSaveService {
    fun save(message: MessageModel): MessageModel?
    fun countUserId(author: String): Int
}