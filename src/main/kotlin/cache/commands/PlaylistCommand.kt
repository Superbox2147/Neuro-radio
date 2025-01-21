package org.whatever.cache.commands

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.embed
import org.whatever.playlistManager
import org.whatever.use

suspend fun playlist(interactionEvent: ChatInputCommandInteractionCreateEvent) {
    interactionEvent.use {
        val interaction = it.interaction
        val response = interaction.deferPublicResponse()
        response.respond {
            embed {
                title = "Playlist"
                description = playlistManager.toString()
                color = Color(0, 127, 255)
            }
        }
    }
}
