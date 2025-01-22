package org.whatever.cache.commands

import dev.kord.common.Color
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.component.option
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed
import org.whatever.cacheManager
import org.whatever.stringToCacheType
import org.whatever.use

suspend fun search(interactionEvent: ChatInputCommandInteractionCreateEvent) {
    interactionEvent.use {
        val interaction = it.interaction
        val command = interaction.command
        val query = command.strings["query"]!!
        val rawType = command.strings["song-type"]!!
        val type = stringToCacheType(rawType)
        val matches = cacheManager.getFiveBestMatches(query, type)
        if (matches.size < 5) {
            val response = interaction.deferPublicResponse()
            response.respond {
                embed {
                    title = "Search"
                    description = "Search failed, not enough results (this should not happen, if it does, please report it immediately)"
                    color = Color(255, 0, 0)
                }
            }
            return@use
        }

        val match1 = matches[0]
        val match2 = matches[1]
        val match3 = matches[2]
        val match4 = matches[3]
        val match5 = matches[4]

        val response = interaction.deferPublicResponse()
        response.respond {
            embed {
                title = "Search"
                description = "Search completed, select an option"
                color = Color(0, 127, 255)
            }
            this.actionRow {
                this.stringSelect(
                    "$rawType-selection"
                ) {
                    this.allowedValues = 1..1
                    option(
                        "${match1.second.second.name} (score: ${match1.first})",
                        match1.second.second.name
                    )
                    option(
                        "${match2.second.second.name} (score: ${match2.first})",
                        match2.second.second.name
                    )
                    option(
                        "${match3.second.second.name} (score: ${match3.first})",
                        match3.second.second.name
                    )
                    option(
                        "${match4.second.second.name} (score: ${match4.first})",
                        match4.second.second.name
                    )
                    option(
                        "${match5.second.second.name} (score: ${match5.first})",
                        match5.second.second.name
                    )
                }
            }
        }
    }
}