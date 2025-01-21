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

    fun addQueue(song: Pair<CacheType, CacheEntry>) {
        queue.addLast(song)
    }

    fun addPriority(song: Pair<CacheType, CacheEntry>) {
        priorityQueue.addLast(song)
    }

    fun nextSong(): Pair<CacheType, CacheEntry> {
        currentlyPlaying = if (priorityQueue.isNotEmpty()) {
            val nextSong = priorityQueue[0]
            priorityQueue.removeFirst()
            nextSong
        } else {
            val nextSong = queue[0]
            queue.removeFirst()
            addQueue(cacheManager.random())
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
        stringBuilder.append("\nCurrently playing:\n${ when(currentlyPlaying.first) {
            CacheType.Neuro -> "Neuro-sama: "
            CacheType.Evil -> "Evil Neuro: "
            CacheType.Duet -> "Neuro duet: "
            CacheType.OtherDuet -> "Other duet: "
        }}${currentlyPlaying.second.name}")
        return stringBuilder.toString()
    }

    fun currentlyPlaying(): Pair<CacheType, CacheEntry> = currentlyPlaying
}