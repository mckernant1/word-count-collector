package com.github.mckernant1.marketeer.collector

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class SiteWorkerKtTest {

    @Test
    fun getWordCountTest() {
        val testString = "aa bb cc aa cc bb"

        assertEquals(2, mapStringToCount(testString)["aa"])
    }

}
