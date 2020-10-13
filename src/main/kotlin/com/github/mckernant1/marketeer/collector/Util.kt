package com.github.mckernant1.marketeer.collector

import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

fun getStringFromFile(filename: String): String =
    object {}::class.java.classLoader.getResource(filename)!!.readText()



private val dbUser: String = System.getenv("MONGO_USER")
        ?: throw Exception("Environment variable MONGO_USER has not been specified")
private val dbPassword: String = System.getenv("MONGO_PASSWORD")
        ?: throw Exception("Environment variable MONGO_PASSWORD has not been specified")

private val wordDatabase = "words"

private val client =
    KMongo.createClient("mongodb+srv://$dbUser:$dbPassword@marketeer.h1pdv.mongodb.net/$wordDatabase?retryWrites=true&w=majority")
val collection = client.getDatabase("words").getCollection<WordDay>("words")


data class WordDay(
    val word: String,
    val wordCount: Int,
    val source: String
)


