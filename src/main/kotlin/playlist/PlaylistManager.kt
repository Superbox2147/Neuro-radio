package org.whatever.playlist

import org.whatever.cache.CacheEntry
import org.whatever.cache.CacheManager
import org.whatever.cache.CacheType
import org.whatever.cacheManager

class PlaylistManager(cacheManager: CacheManager) {
    private val queue = mutableListOf<Pair<CacheType, CacheEntry>>()
    private val priorityQueue = mutableListOf<Pair<CacheType, CacheEntry>>()

    private var currentlyPlaying: Pair<CacheType, CacheEntry>

    init {
        cacheManager.updateCache()
        for (i in 1..10) {
            addQueue(cacheManager.random())
        }
        currentlyPlaying = cacheManager.random()
    }

    private fun addQueue(song: Pair<CacheType, CacheEntry>) {
        queue.addLast(song)
    }

    fun addPriority(song: Pair<CacheType, CacheEntry>) {
        priorityQueue.addLast(song)
    }

    private fun getSongNotInQueue(): Pair<CacheType, CacheEntry> {
        var newSong = cacheManager.random()
        while (queue.contains(newSong)) {
            newSong = cacheManager.random()
        }
        return newSong
    }

    fun nextSong(): Pair<CacheType, CacheEntry> {
        currentlyPlaying = if (priorityQueue.isNotEmpty()) {
            val nextSong = priorityQueue[0]
            priorityQueue.removeFirst()
            nextSong
        } else {
            val nextSong = queue[0]
            queue.removeFirst()
            val newSong = getSongNotInQueue()
            addQueue(newSong)
            nextSong
        }
        return currentlyPlaying
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Queue:")
        for (i in queue.reversed()) {
            stringBuilder.append("\n${ when(i.first) {
                CacheType.Neuro -> "Neuro-sama: "
                CacheType.Evil -> "Evil Neuro: "
                CacheType.Duet -> "Neuro duet: "
                CacheType.OtherDuet -> "Other duet: "
            }            }${i.second.name}")
        }
        if (priorityQueue.isNotEmpty()) {
            stringBuilder.append("\n\nPriority queue:")
            for (i in priorityQueue.reversed()) {
                stringBuilder.append("\n${ when(i.first) {
                    CacheType.Neuro -> "Neuro-sama: "
                    CacheType.Evil -> "Evil Neuro: "
                    CacheType.Duet -> "Neuro duet: "
                    CacheType.OtherDuet -> "Other duet: "
                }}${i.second.name}")
            }
        }
        stringBuilder.append("\n\nCurrently playing:\n${ when(currentlyPlaying.first) {
            CacheType.Neuro -> "Neuro-sama: "
            CacheType.Evil -> "Evil Neuro: "
            CacheType.Duet -> "Neuro duet: "
            CacheType.OtherDuet -> "Other duet: "
        }}${currentlyPlaying.second.name}")
        return stringBuilder.toString()
    }

    fun currentlyPlaying(): Pair<CacheType, CacheEntry> = currentlyPlaying

    fun addSongWithQuery(query: String, type: CacheType): Pair<Int, Pair<CacheType, CacheEntry>>? {
        val newSong = cacheManager.query(query, type) ?: return null
        addPriority(newSong.second)
        return newSong
    }
}