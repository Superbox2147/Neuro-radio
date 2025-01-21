package org.whatever.cache

import kotlinx.serialization.Serializable

@Serializable
data class CacheEntry(val name: String, val fileName: String)