package org.whatever.cache.commands

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.embed
import org.whatever.cacheManager
import org.whatever.use

suspend fun reload(interactionEvent: ChatInputCommandInteractionCreateEvent) {
    interactionEvent.use {
        val interaction = it.interaction
        val response = interaction.deferPublicResponse()
        cacheManager.updateCache()
        response.respond {
            embed {
                title = "Reload"
                description = "Reloaded caches"
                color = Color(0, 127, 255)
            }
        }
    }
}