package com.github.rcd27.youtube.streams

import com.github.rcd27.YouTubeClient
import com.github.rcd27.youtube.channel.ChannelId
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

context(client: YouTubeClient)
suspend fun ChannelId.getAllCompletedStreams(): List<String> {
    val urls = mutableListOf<String>()
    var pageToken: String? = null

    do {
        val response = client.http.get("https://www.googleapis.com/youtube/v3/search") {
            parameter("part", "snippet")
            parameter("channelId", channelId)
            parameter("eventType", "completed")
            parameter("type", "video")
            parameter("maxResults", 100)
            parameter("key", client.apiKey)
            pageToken?.let { parameter("pageToken", it) }
        }.body<GetCompletedStreamsResponse>()

        response.items
            .map { it.id.videoId }
            .map { "https://www.youtube.com/watch?v=$it" }
            .also { urls += it }

        pageToken = response.nextPageToken
    } while (pageToken != null)

    return urls
}

@Serializable
data class GetCompletedStreamsResponse(
    @SerialName("nextPageToken")
    val nextPageToken: String? = null,

    @SerialName("items")
    val items: List<CompletedStreamItem> = emptyList()
)

@Serializable
data class CompletedStreamItem(
    @SerialName("id")
    val id: CompletedStreamId
)

@Serializable
data class CompletedStreamId(
    @SerialName("videoId")
    val videoId: String
)