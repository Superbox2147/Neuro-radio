package org.whatever.cache

enum class CacheType {
    Neuro,
    Evil,
    Duet,
    OtherDuet;

    companion object {
        fun random(): CacheType = listOf(Neuro, Evil, Duet, OtherDuet).random()
    }
}