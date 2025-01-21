package org.whatever.cache

import kotlinx.serialization.Serializable

@Serializable
data class Cache(val data: List<CacheEntry>) {
    fun random(): CacheEntry = data.random()
}