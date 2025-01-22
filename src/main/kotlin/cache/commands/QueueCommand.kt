package org.whatever.cache.commands

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.embed
import org.whatever.playlistManager
import org.whatever.stringToCacheType
import org.whatever.use

suspend fun queue(interactionEvent: ChatInputCommandInteractionCreateEvent) {
    interactionEvent.use {
        val interaction = it.interaction
        val command = interaction.command
        val query = command.strings["query"]!!
        val type = stringToCacheType(command.strings["song-type"]!!)
        val addedSong = playlistManager.addSongWithQuery(query, type)
        val response = interaction.deferPublicResponse()
        if (addedSong == null) {
            response.respond {
                embed {
                    title = "Queue"
                    description = "Query \"$query\" has no accurate match (score over 75)"
                    color = Color(255, 0, 0)
                }
            }
            return@use
        }
        response.respond {
            embed {
                title = "Queue"
                description = "Added song ${addedSong.second.second.name} (score: ${addedSong.first})"
                color = Color(0, 127, 255)
            }
        }
    }
}