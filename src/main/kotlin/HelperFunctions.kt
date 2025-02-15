package org.whatever

import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import org.whatever.cache.CacheType

fun getCommandFullName(command: InteractionCommand): String {
    if (command.data.options.value
            ?.first()
            ?.value
            ?.value != null
    ) {
        return command.rootName
    }
    if (command.data.options.value
            ?.first() == null
    ) {
        return command.rootName
    }
    return "${command.rootName} ${command.data.options.value?.first()?.name}"
}

suspend fun ChatInputCommandInteractionCreateEvent.use(block: suspend (ChatInputCommandInteractionCreateEvent) -> Unit) {
    block(this@use)
}

fun stringToCacheType(string: String): CacheType {
    return when(string) {
        "neuro" -> CacheType.Neuro
        "evil" -> CacheType.Evil
        "duet" -> CacheType.Duet
        "other" -> CacheType.OtherDuet
        else -> throw IllegalArgumentException("$string is not valid for a CacheType")
    }
}