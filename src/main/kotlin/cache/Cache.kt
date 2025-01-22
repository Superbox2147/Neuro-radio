package org.whatever.cache

import kotlinx.serialization.Serializable

@Serializable
data class Cache(val data: List<CacheEntry>) {
    fun random(): CacheEntry = data.random()

    fun toStringList(): List<String> = buildList {
        for (i in data) {
            add(i.name)
        }
    }

    fun getByName(name: String): CacheEntry? {
        for (i in data) {
            if (i.name == name) {
                return i
            }
        }
        return null
    }
}