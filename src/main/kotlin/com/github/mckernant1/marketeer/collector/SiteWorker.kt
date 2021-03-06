package com.github.mckernant1.marketeer.collector

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.time.ZonedDateTime

private val logger = LoggerFactory.getLogger("SweeperLogger")

internal fun sweepSite(siteUrl: String): List<WordDay> {
    val wordsToIgnore = Json.decodeFromString<List<String>>(
        getStringFromFile(BASIC_WORDS_FILENAME)
    )
    logger.info("Getting raw html for '$siteUrl'")
    val doc = Jsoup.connect(siteUrl)
        .get()
        .body()
        .text()

    return mapStringToCount(doc) {
        it.length >= 3 && !wordsToIgnore.contains(it.toLowerCase())
    }.map { (word, count) ->
        WordDay(
            word,
            count,
            siteUrl,
            ZonedDateTime.now(ZoneId.of("America/Los_Angeles"))
        )
    }.filter { it.wordCount > 4 }

}

internal fun mapStringToCount(string: String, filter: (String) -> Boolean = { true }) = string
    .replace(Regex("[^a-z A-Z]"), "")
    .split(Regex("\\s"))
    .filter(filter)
    .groupingBy { it }
    .eachCount()
