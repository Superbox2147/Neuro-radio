package org.whatever.cache

import kotlinx.serialization.Serializable

@Serializable
data class CacheFile(val caches: Map<CacheType, Cache>) {
    operator fun get(type: CacheType): Cache = caches[type]!!
}