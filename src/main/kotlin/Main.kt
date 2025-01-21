package org.whatever

import dev.kord.common.entity.PresenceStatus
import dev.kord.common.entity.Snowflake
import dev.kord.gateway.Intent
import io.github.cdimascio.dotenv.dotenv
import org.whatever.cache.CacheManager
import org.whatever.cache.playback.VoiceChannelRadioManager
import org.whatever.playlist.PlaylistManager

val dotenv = dotenv()
val kord = generateKord()
val cacheManager = CacheManager()
val playlistManager = PlaylistManager(cacheManager)
val voiceChannelRadios = mutableMapOf<Snowflake, VoiceChannelRadioManager>()


suspend fun main() {
    setSlashCommands(kord)
    setCommandListeners(kord)

    kord.login {
        intents += Intent.GuildVoiceStates
        presence {
            listening("Neuro covers")
            status = PresenceStatus.Online
        }
    }
}