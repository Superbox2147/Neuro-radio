package org.whatever.cache.playback

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.channel.BaseVoiceChannelBehavior
import dev.kord.voice.AudioFrame
import dev.kord.voice.VoiceConnection
import kotlinx.coroutines.runBlocking
import org.whatever.cache.classExtensions.playTrack
import org.whatever.kord
import org.whatever.playlistManager

@OptIn(KordVoice::class)
class VoiceChannelRadioManager(private val voiceChannel: BaseVoiceChannelBehavior) {
    private var connection: VoiceConnection? = null

    private val lavaplayerManager = DefaultAudioPlayerManager()
    private var player: AudioPlayer? = null

    init {
        try {
            runBlocking {
                AudioSourceManagers.registerLocalSource(lavaplayerManager)

                player = lavaplayerManager.createPlayer()

                player!!.addListener(RadioPlayer(lavaplayerManager))

                lavaplayerManager.playTrack(playlistManager.currentlyPlaying().second.fileName, player!!)

                println("Create voice connection")

                val newConnection = VoiceConnection(
                    kord.gateway.gateways.values.random(),
                    kord.selfId,
                    voiceChannel.id,
                    voiceChannel.guildId
                ) {
                    audioProvider {
                        AudioFrame.fromData(player!!.provide()?.data)
                    }
                }
                newConnection.connect()

                connection = newConnection
            }
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace()
        }
    }

    suspend fun skip() {
        println("Skipping track ${playlistManager.currentlyPlaying()}")
        player!!.stopTrack()
        val newTrack = playlistManager.nextSong()
        lavaplayerManager.playTrack(newTrack.second.fileName, player!!)
    }

    suspend fun dispose() {
        connection!!.shutdown()
    }
}