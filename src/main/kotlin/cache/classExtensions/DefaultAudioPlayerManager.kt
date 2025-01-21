package org.whatever.cache.classExtensions

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.exitProcess


suspend fun DefaultAudioPlayerManager.playTrack(filename: String, player: AudioPlayer): AudioTrack {
    try {
        val track = suspendCoroutine<AudioTrack> {
            this.loadItem(filename, object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack) {
                    println("Track loaded ${track.info.uri}")
                    it.resume(track)
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {
                    it.resume(playlist.tracks.first())
                }

                override fun noMatches() {
                    TODO()
                }

                override fun loadFailed(exception: FriendlyException?) {
                    println("Load failed with reason ${exception?.message}")
                }
            })
        }
        player.playTrack(track)

        return track
    } catch (e: Exception) {
        println(e.toString())
        e.printStackTrace()
        exitProcess(1)
    }
}