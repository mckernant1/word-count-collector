package com.github.mckernant1.marketeer.collector

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZonedDateTime

fun getStringFromFile(filename: String): String =
    object {}::class.java.classLoader.getResource(filename)!!.readText()



//private val connectionString: String = System.getenv("MONGO_CONNECTION_STRING")
//        ?: throw Exception("Environment variable MONGO_CONNECTION_STRING has not been specified")
//
//private val client = KMongo.createClient(connectionString)
//val collection =
//    client.getDatabase("words").getCollection<WordDay>("words")

@Serializable
data class WordDay(
    val word: String,
    val wordCount: Int,
    val source: String,
    @Serializable(ZonedDateTimeSerializer::class) val timeStamp: ZonedDateTime
)

class ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ZonedDateTime {
       return ZonedDateTime.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.toString())
    }

}
