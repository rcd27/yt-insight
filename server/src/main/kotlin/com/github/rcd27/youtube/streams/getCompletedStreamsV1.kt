package com.github.rcd27.youtube.streams

import com.github.rcd27.YouTubeClient
import com.github.rcd27.youtube.channel.ChannelId
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import java.io.File

context(client: YouTubeClient)
suspend fun ChannelId.getAllCompletedStreamsV1(
    limit: Int = Int.MAX_VALUE
): Result<List<String>> = runCatching {
    val urls = mutableListOf<String>()
    var pageToken: String? = null
    var pageCount = 0
    val maxPages = 200

    do {
        if (++pageCount > maxPages) {
            println("Reached max pages limit for channel $channelId")
            break
        }

        val response = fetchPage(pageToken)
            .getOrElse { error ->
                println("Failed to fetch page $pageCount: $error")
                throw error
            }

        response.items
            .take((limit - urls.size).coerceAtLeast(0))
            .mapTo(urls) { "https://www.youtube.com/watch?v=${it.id.videoId}" }

        pageToken = response.nextPageToken

        if (urls.size >= limit) break

    } while (pageToken != null)

    println("Fetched ${urls.size} streams for channel $channelId")
    urls
}

context(client: YouTubeClient)
private suspend fun ChannelId.fetchPage(pageToken: String?) = runCatching {
    client.http.get("https://www.googleapis.com/youtube/v3/search") {
        parameter("part", "snippet")
        parameter("channelId", channelId)
        parameter("type", "video")
        parameter("order", "date")
        parameter("maxResults", 50)
        parameter("key", client.apiKey)
        pageToken?.let { parameter("pageToken", it) }
    }.body<GetCompletedStreamsResponse>()
}

fun main() {
    runBlocking {
        val channelId = ChannelId("UCSQGkViib9XLwjDfIahtLdw")
        with(YouTubeClient) {
            val result: List<String> = channelId.getAllCompletedStreamsV1(50).getOrThrow()
            val file = File("streams.txt")
            file.createNewFile()
            file.writeText(result.joinToString("\n"))
        }
    }
}