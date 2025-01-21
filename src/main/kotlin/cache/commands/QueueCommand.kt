package org.whatever.cache.commands

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.embed
import org.whatever.use

suspend fun queue(interactionEvent: ChatInputCommandInteractionCreateEvent) {
    interactionEvent.use {
        val interaction = it.interaction
        val response = interaction.deferPublicResponse()
        response.respond {
            embed {
                title = "Queue"
                description = "Not implemented yet"
                color = Color(255, 0, 0)
            }
        }
    }
}