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
    val doc = try {
        Jsoup.connect(siteUrl)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .header("Accept-Language", "en-US,en;q=0.9")
            .header("Accept-Encoding", "gzip, deflate, br")
            .timeout(5000)
            .ignoreContentType(true)
            .followRedirects(true)
            .get()
            .body()
            .text()
    } catch (e: Exception) {
        throw RuntimeException("Could not get html for '$siteUrl'", e)
    }
    return mapStringToCount(doc) {
        it.length >= 3 && !wordsToIgnore.contains(it.lowercase())
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
