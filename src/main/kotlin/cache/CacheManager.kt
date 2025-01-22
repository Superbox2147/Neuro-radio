package org.whatever.cache

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.whatever.extraClasses.FilePaths
import java.io.File

class CacheManager {
    private val cacheFile = File("./caches/SongQueryCache.json")
    init {
        if (!File(cacheFile.parent).exists()) {
            File(cacheFile.parent).mkdir()
        }
        if (!cacheFile.exists()) {
            cacheFile.createNewFile()
            cacheFile.writeText(Json.encodeToString(CacheFile(mapOf())))
        }
    }

    private val songPaths = Json.decodeFromString<FilePaths>(File("./configs/folders.json").readText())

    private fun removeExtension(fileName: String): String = fileName.split(".").dropLast(1).joinToString(".")

    private fun generateCacheFromPaths(paths: List<String>): CacheFile {
        val neuroSongs = mutableListOf<CacheEntry>()
        val evilSongs = mutableListOf<CacheEntry>()
        val duetSongs = mutableListOf<CacheEntry>()
        val otherDuets = mutableListOf<CacheEntry>()

        paths.forEach { path ->
            for (file in File(path).listFiles()!!){
                if (file.isFile) {
                    val fileName = file.name
                    if (fileName.startsWith("NeuroSama")) {
                        neuroSongs.add(CacheEntry(removeExtension(fileName.removePrefix("NeuroSama").removePrefix("-")), file.path))
                        continue
                    }
                    if (fileName.startsWith("EvilNeuro")) {
                        evilSongs.add(CacheEntry(removeExtension(fileName.removePrefix("EvilNeuro").removePrefix("-")), file.path))
                        continue
                    }
                    if ((fileName.startsWith("Neuro x ") || fileName.startsWith("Evil x ")) && (!fileName.startsWith("Neuro x Evil"))) {
                        otherDuets.add(CacheEntry(removeExtension(fileName), file.path))
                        continue
                    }
                    if (fileName.startsWith("Neuro x Evil")) {
                        duetSongs.add(CacheEntry(removeExtension(fileName.removePrefix("Neuro x Evil ")), file.path))
                    }
                }
            }
        }

        return CacheFile(buildMap {
            put(CacheType.Neuro, Cache(neuroSongs))
            put(CacheType.Evil, Cache(evilSongs))
            put(CacheType.Duet, Cache(duetSongs))
            put(CacheType.OtherDuet, Cache(otherDuets))
        })
    }

    fun updateCache() {
        cacheFile.writeText(Json.encodeToString(generateCacheFromPaths(listOf(songPaths.official, songPaths.rvc))))
    }

    private fun readCache(type: CacheType): Cache = try {
        Json.decodeFromString<CacheFile>(cacheFile.readText())[type]
    } catch (e: NullPointerException) {
        updateCache()
        Json.decodeFromString<CacheFile>(cacheFile.readText())[type]
    }

    operator fun get(type: CacheType): Cache = readCache(type)

    fun random(): Pair<CacheType, CacheEntry> = run { val type = CacheType.random(); Pair(type,readCache(type).random()) }

    fun query(query: String, type: CacheType): Pair<Int, Pair<CacheType, CacheEntry>>? {
        val cacheData = readCache(type)
        val match = FuzzySearch.extractOne(query, cacheData.toStringList())
        return if (match.score >= 75) {
            Pair(match.score, Pair(type, cacheData.getByName(match.string)!!))
        } else null
    }

    fun getFiveBestMatches(query: String, type: CacheType): List<Pair<Int, Pair<CacheType, CacheEntry>>> {
        val cacheData = readCache(type)
        val matches = FuzzySearch.extractSorted(query, cacheData.toStringList(), 5)
        return buildList {
            for (match in matches) {
                add(Pair(match.score, Pair(type, cacheData.getByName(match.string)!!)))
            }
        }
    }
}