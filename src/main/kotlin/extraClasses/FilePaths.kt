package org.whatever.extraClasses

import kotlinx.serialization.Serializable

@Serializable
data class FilePaths(val official: String, val rvc: String)