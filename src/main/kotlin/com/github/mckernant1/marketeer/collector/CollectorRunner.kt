package com.github.mckernant1.marketeer.collector

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


const val SITES_LIST_FILENAME: String = "sites-list.json"
const val BASIC_WORDS_FILENAME: String = "basic-words.json"
const val BUCKET_NAME: String = "mckernant1-marketeer"
const val FOLDER_NAME: String = "news-words"

private val logger = LoggerFactory.getLogger("LauncherLogger")
private val s3 = S3Client.create()


fun main() = runBlocking {
    val sitesList = Json.decodeFromString<List<String>>(
        getStringFromFile(SITES_LIST_FILENAME)
    )
    val subFolder = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH")
        .format(ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS))

    for (site in sitesList) {
        logger.info(
            "Starting sweeper for '$site'"
        )
        val words = try {
            sweepSite(site)
        } catch (e: Exception) {
            logger.warn("Could not get words for '$site'", e)
            continue
        }
        s3.putObject({
            it.bucket(BUCKET_NAME)
            it.key("$FOLDER_NAME/${subFolder}/${site.dropLast(1).removePrefix("https://www.")}.json")
        }, RequestBody.fromString(Json.encodeToString(words)))
    }
    logger.info("Finishing...")
}
