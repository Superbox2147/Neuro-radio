package org.whatever.cache.commands

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.embed
import kotlinx.coroutines.flow.first
import org.whatever.cache.playback.VoiceChannelRadioManager
import org.whatever.kord
import org.whatever.use
import org.whatever.voiceChannelRadios

suspend fun reconnect(interactionEvent: ChatInputCommandInteractionCreateEvent) {
    interactionEvent.use {
        val interaction = it.interaction
        val guildId = interaction.channel.messages.first().getGuild().id
        val radio = voiceChannelRadios[guildId]
        if (radio == null) {
            val response = interaction.deferEphemeralResponse()
            response.respond {
                embed {
                    title = "Reconnect"
                    description = "No radio active on server"
                    color = Color(255, 0, 0)
                }
            }
            return@use
        }
        radio.dispose()
        val voiceChannel = kord.getSelf().asMember(guildId).getVoiceStateOrNull()?.getChannelOrNull()
        if (voiceChannel == null) {
            val response = interaction.deferEphemeralResponse()
            response.respond {
                embed {
                    title = "Reconnect"
                    description = "Use `/join` instead"
                    color = Color(255, 0, 0)
                }
            }
            return@use
        }
        voiceChannelRadios[guildId] = VoiceChannelRadioManager(voiceChannel)
        val response = interaction.deferPublicResponse()
        response.respond {
            embed {
                title = "Reconnect"
                description = "Reconnected"
                color = Color(0, 127, 255)
            }
        }
    }
}