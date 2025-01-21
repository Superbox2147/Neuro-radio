package org.whatever.cache.playback

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import kotlinx.coroutines.runBlocking
import org.whatever.cache.classExtensions.playTrack
import org.whatever.playlistManager


class RadioPlayer(private val lavaPlayerManager: DefaultAudioPlayerManager) : AudioEventAdapter() {
    override fun onPlayerPause(player: AudioPlayer?) {
        // Player was paused
        println("Player pause")
    }

    override fun onPlayerResume(player: AudioPlayer?) {
        // Player was resumed
        println("Play resume")
    }

    override fun onTrackStart(player: AudioPlayer, track: AudioTrack?) {
        // A track started playing
        println("Started playing ${track?.info?.uri} (${playlistManager.currentlyPlaying()})")
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack?, endReason: AudioTrackEndReason) = runBlocking {
        if (endReason.mayStartNext) {
            // Start next track
            val nextSong = playlistManager.nextSong()
            println("Play ${nextSong.second.name}")
            lavaPlayerManager.playTrack(nextSong.second.fileName, player)
        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    override fun onTrackException(player: AudioPlayer?, track: AudioTrack?, exception: FriendlyException?) {
        println("${track?.info?.title} failed playing with reason ${exception?.message} (${exception?.severity})")
        // An already playing track threw an exception (track end event will still be received separately)
    }

    override fun onTrackStuck(player: AudioPlayer, track: AudioTrack?, thresholdMs: Long) = runBlocking {
        println("${track?.info?.title} stuck, overriding track")
        // Audio track has been unable to provide us any audio, might want to just start a new track
        val nextSong = playlistManager.nextSong()
        lavaPlayerManager.playTrack(nextSong.second.fileName, player)
        Unit
    }
}