package org.whatever

import dev.kord.common.entity.ChannelType
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.string
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
                "reload" -> cacheManager.updateCache()
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
}