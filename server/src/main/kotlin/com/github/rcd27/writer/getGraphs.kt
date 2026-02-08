package com.github.rcd27.writer

import com.github.rcd27.Config
import com.github.rcd27.YouTubeClient
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun getGraphs(): GetGraphsResponse {
    // TODO: подшаманить HttpClient, либо достать его из YouTubeClient, ну кароч
    // TODO: вынести заголовок в дефолтный запрос HTTP клиента
    return YouTubeClient.http.get("https://api.writer.com/v1/graphs") {
        header("Authorization", "Bearer ${Config.WRITER_AI_API_KEY}")
    }.body()
}