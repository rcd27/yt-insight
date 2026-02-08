package com.github.rcd27.ytinsight

import com.github.rcd27.ytinsight.youtube.channel.resolveChannelId
import com.github.rcd27.ytinsight.youtube.streams.getAllCompletedStreams
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// FIXME: довольно спорное решение, но сейчас не хотелось бы на этом заморачиваться
object YouTubeClient {
    val http = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )

        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }
    val apiKey = "OOPS"
}


suspend fun main() {
    with(YouTubeClient) {
        val channelId = resolveChannelId("https://www.youtube.com/@GOPsterPlayTV")
        val streamUrls = channelId.getAllCompletedStreams()
    }
}
