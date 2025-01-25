package org.whatever

import dev.kord.common.Color
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Choice
import dev.kord.common.entity.InteractionType
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.Event
import dev.kord.core.event.interaction.ActionInteractionCreateEvent
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.ModalSubmitInteractionCreateEvent
import dev.kord.core.event.interaction.SelectMenuInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.embed
import kotlinx.coroutines.runBlocking
import org.whatever.cache.commands.*
import kotlin.system.exitProcess

fun generateKord(): Kord = runBlocking {
    try {
        return@runBlocking Kord(dotenv["TOKEN"]!!)
    } catch (e: NullPointerException) {
        println("Token is not set")
        exitProcess(1)
    }
}

suspend fun setSlashCommands(kord: Kord) {
    println("Define commands")
    kord.createGlobalChatInputCommand(
        "reload",
        "Reload the caches used for query and playlist selection"
    )
    kord.createGlobalChatInputCommand(
        "playlist",
        "Display the current playlist"
    )
    kord.createGlobalChatInputCommand(
        "join",
        "Join the voice channel you are in"
    )
    kord.createGlobalChatInputCommand(
        "queue",
        "Queue a song"
    ) {
        string(
            "query",
            "Song query"
        ) {
            required = true
        }
        string(
            "song-type",
            "Weather to look for Neuro or Evil covers, Neuro x Evil duets or other duets"
        ) {
            required = true
            choice(
                "neuro",
                "neuro"
            )
            choice(
                "evil",
                "evil"
            )
             choice(
                 "neuro-duet",
                 "duet"
             )
            choice(
                "other-duet",
                "other"
            )
        }
    }
    kord.createGlobalChatInputCommand(
        "search",
        "Search for a song, returns multiple options to choose from"
    ) {
        string(
            "query",
            "Song query"
        ) {
            required = true
        }
        string(
            "song-type",
            "Weather to look for Neuro or Evil covers, Neuro x Evil duets or other duets"
        ) {
            required = true
            choice(
                "neuro",
                "neuro"
            )
            choice(
                "evil",
                "evil"
            )
            choice(
                "neuro-duet",
                "duet"
            )
            choice(
                "other-duet",
                "other"
            )
        }
    }
    kord.createGlobalChatInputCommand(
        "skip",
        "Skip the currently playing song"
    )
}

suspend fun setCommandListeners(kord: Kord) {
    println("Define listeners")
    kord.on<ChatInputCommandInteractionCreateEvent> {
        try {
            if (this.interaction.channel.asChannel().type == ChannelType.DM) {
                val response = interaction.deferEphemeralResponse()
                response.respond {
                    content = "This bot does not work in DMs"
                }
            }
            val commandName = getCommandFullName(interaction.command)
            when (commandName.split(" ")[0]) {
                "reload" -> reload(this)
                "playlist" -> playlist(this)
                "join" -> join(this)
                "queue" -> queue(this)
                "search" -> search(this)
                "skip" -> skip(this)
            }
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace()
        }
    }
    kord.on<SelectMenuInteractionCreateEvent> {
        try {
            val songName = interaction.values[0]
            val type = interaction.data.data.customId.value?.removeSuffix("-selection")?.let { stringToCacheType(it) }
            type!!
            val song = cacheManager[type].getByName(songName)
            playlistManager.addPriority(Pair(type, song!!))
            val response = interaction.deferPublicResponse()
            response.respond {
                embed {
                    title = "Song added"
                    description = "Song $songName added to priority queue"
                    color = Color(0, 127, 255)
                }
            }
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace()
        }
    }
}