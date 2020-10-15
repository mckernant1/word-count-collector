package com.github.mckernant1.marketeer.collector

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.litote.kmongo.save
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneId

private val logger = LoggerFactory.getLogger("SweeperLogger")


internal suspend fun sweepSite(siteUrl: String) {
    val wordsToIgnore = Json.decodeFromString<List<String>>(
        getStringFromFile(BASIC_WORDS_FILENAME)
    )
    logger.info("Getting raw html for '$siteUrl'")
    val doc = Jsoup.connect(siteUrl)
        .get()
        .body()
        .text()

    val wordDays = mapStringToCount(doc) {
        it.length >= 3 && !wordsToIgnore.contains(it)
    }.map { (word, count) ->
        WordDay(
            word,
            count,
            siteUrl,
            LocalDateTime.now().atZone(ZoneId.of("America/Los_Angeles"))
        )
    }.filter { it.wordCount > 4 }

    logger.info("Inserting ${wordDays.size} words into collection")
    collection.insertMany(wordDays)
}

internal fun mapStringToCount(string: String, filter: (String) -> Boolean = { true }) = string
    .replace(Regex("[^a-z A-Z]"), "")
    .split(Regex("\\s"))
    .filter(filter)
    .groupingBy { it }
    .eachCount()
