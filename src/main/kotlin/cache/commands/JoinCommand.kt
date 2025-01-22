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

suspend fun join(interactionEvent: ChatInputCommandInteractionCreateEvent) {
    interactionEvent.use {
        val interaction = it.interaction
        val user = interaction.user
        val voiceChannel = user.fetchMember(interaction.channel.messages.first().getGuild().id).getVoiceStateOrNull()?.getChannelOrNull()
        if (voiceChannel == null) {
            val response = interaction.deferEphemeralResponse()
            response.respond {
                embed {
                    description = "Could not join voice channel, check that you are in a voice channel and try again"
                    color = Color(255, 0, 0)
                }
            }
            return@use
        }

        if (voiceChannel.id == kord.getSelf().asMember(interaction.channel.messages.first().getGuild().id).getVoiceStateOrNull()?.channelId) {
            val response = interaction.deferEphemeralResponse()
            response.respond {
                embed {
                    description = "Bot is already active on that voice channel"
                    color = Color(255, 0, 0)
                }
            }
            return@use
        }

        val response = interaction.deferPublicResponse()
        response.respond {
            embed {
                title = "Joining..."
                description = "Joining voice channel"
                color = Color(0, 127, 255)
            }
        }

        if (voiceChannelRadios[voiceChannel.guildId] != null) {
            voiceChannelRadios[voiceChannel.guildId]?.dispose()
        }

        val radio = VoiceChannelRadioManager(voiceChannel)
        voiceChannelRadios[voiceChannel.guildId] = radio
    }
}