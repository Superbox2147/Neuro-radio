package org.whatever.cache

enum class CacheType {
    Neuro,
    Evil,
    Duet,
    OtherDuet;

    companion object {
        fun random(): CacheType = listOf(Neuro, Neuro, Neuro, Neuro, Evil, Evil, Evil, Evil, Duet, Duet, OtherDuet).random()
    }
}