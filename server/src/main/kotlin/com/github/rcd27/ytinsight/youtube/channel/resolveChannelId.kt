package com.github.rcd27.ytinsight.youtube.channel

import com.github.rcd27.ytinsight.YouTubeClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

context(client: YouTubeClient)
suspend fun resolveChannelId(channelUrl: String): ChannelId {
    val hint = extractChannelHint(channelUrl)

    // Уже channel_id
    if (hint.value.startsWith("UC")) {
        return ChannelId(hint.value)
    }

    val response: ChannelSearchResponse =
        client.http.get("https://www.googleapis.com/youtube/v3/search") {
            parameter("part", "snippet")
            parameter("type", "channel")
            parameter("q", hint)
            parameter("maxResults", 1)
            parameter("key", client.apiKey)
        }.body()

    return response.items.firstOrNull()?.id ?: error("Channel not found for: $channelUrl")
}

@Serializable
data class ChannelSearchResponse(
    val items: List<ChannelSearchItem>
)

@Serializable
data class ChannelSearchItem(
    val id: ChannelId
)

@Serializable
data class ChannelId(
    @SerialName("channelId")
    val channelId: String
)