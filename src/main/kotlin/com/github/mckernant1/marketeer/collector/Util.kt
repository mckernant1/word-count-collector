package com.github.mckernant1.marketeer.collector

import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

fun getStringFromFile(filename: String): String =
    object {}::class.java.classLoader.getResource(filename)!!.readText()



private val connectionString: String = System.getenv("MONGO_CONNECTION_STRING")
        ?: throw Exception("Environment variable MONGO_USER has not been specified")

private val client = KMongo.createClient(connectionString)
val collection =
    client.getDatabase("words").getCollection<WordDay>("words")


data class WordDay(
    val word: String,
    val wordCount: Int,
    val source: String
)


