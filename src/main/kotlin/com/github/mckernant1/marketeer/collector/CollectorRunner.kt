package com.github.mckernant1.marketeer.collector

import com.github.shyiko.skedule.Schedule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.time.*
import java.util.*
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule


const val SITES_LIST_FILENAME: String = "sites-list.json"
const val BASIC_WORDS_FILENAME: String = "basic-words.json"

private val logger = LoggerFactory.getLogger("LauncherLogger")

fun main() {
    val sitesList = Json.decodeFromString<List<String>>(
        getStringFromFile(SITES_LIST_FILENAME)
    )
    sitesList.forEach {
        logger.info(
            "Starting sweeper for '$it' at ${
                LocalDateTime.now().atZone(ZoneId.of("America/Los_Angeles"))
            }"
        )
        GlobalScope.launch { sweepSite(it) }
    }

}

