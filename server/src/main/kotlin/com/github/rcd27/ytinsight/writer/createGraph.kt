package com.github.rcd27.ytinsight.writer

import com.github.rcd27.ytinsight.Config
import com.github.rcd27.ytinsight.YouTubeClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGraphRequest(
    val name: String,
    val description: String
)

@Serializable
data class CreateGraphResponse(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    val name: String,
    val description: String
)

suspend fun createGraph(name: String, description: String): CreateGraphResponse {
    return YouTubeClient.http.post("https://api.writer.com/v1/graphs") {
        header(HttpHeaders.Authorization, "Bearer ${Config.WRITER_AI_API_KEY}")
        header(HttpHeaders.ContentType, "application/json")
        contentType(ContentType.Application.Json)
        setBody(CreateGraphRequest(name, description))
    }.body()
}