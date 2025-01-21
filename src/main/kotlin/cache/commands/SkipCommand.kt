package org.whatever.cache.commands

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.embed
import kotlinx.coroutines.flow.first
import org.whatever.use
import org.whatever.voiceChannelRadios

suspend fun skip(interactionEvent: ChatInputCommandInteractionCreateEvent) {
    interactionEvent.use {
        val interaction = it.interaction
        val guildId = interaction.channel.messages.first().getGuild().id
        val radio = voiceChannelRadios[guildId]
        if (radio == null) {
            val response = interaction.deferEphemeralResponse()
            response.respond {
                embed {
                    title = "Skip"
                    description = "No radio active on server"
                    color = Color(255, 0, 0)
                }
            }
            return@use
        }
        radio.skip()
        val response = interaction.deferPublicResponse()
        response.respond {
            embed {
                title = "Skip"
                description = "Skipped track"
                color = Color(0, 127, 255)
            }
        }
    }
}