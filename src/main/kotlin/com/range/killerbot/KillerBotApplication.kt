package com.range.killerbot

import com.range.killerbot.properties.BotProperties
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.range")
class KillerBotApplication

fun main(args: Array<String>) {
    runApplication<KillerBotApplication>(*args)
}
