package com.github.mckernant1.marketeer.collector

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*
import kotlin.concurrent.schedule

const val SITES_LIST_FILENAME: String = "sites-list.json"
const val BASIC_WORDS_FILENAME: String = "basic-words.json"

private val logger = LoggerFactory.getLogger("LauncherLogger")

suspend fun main(): Unit = coroutineScope {
    val sitesList = Json.decodeFromString<List<String>>(
        getStringFromFile(SITES_LIST_FILENAME)
    )
    val timer = Timer()

    timer.schedule(0, Duration.ofDays(1).toMillis()) {
        sitesList.forEach {
            logger.info("Starting sweeper for '$it'")
            GlobalScope.launch { sweepSite(it) }
        }
    }
}

