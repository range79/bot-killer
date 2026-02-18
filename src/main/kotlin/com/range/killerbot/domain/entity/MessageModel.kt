package com.range.killerbot.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
@OptIn(ExperimentalUuidApi::class)
@RedisHash("message")
data class MessageModel(
    @Id
    var id: Uuid,
    var text: String,
    val author: String,
    @TimeToLive
    var ttl: Long


) : Serializable