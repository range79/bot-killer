package com.range.killerbot.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

@RedisHash("message")
data class MessageModel(
    @Id
    var id: String,
    var text: String,
    val author: String,
    val timestamp: TimeUnit,

    @TimeToLive
    var ttl: Long = 30


) : Serializable