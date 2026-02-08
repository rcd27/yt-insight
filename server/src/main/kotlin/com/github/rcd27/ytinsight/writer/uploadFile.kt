package com.github.rcd27.ytinsight.writer

import com.github.rcd27.ytinsight.Config
import com.github.rcd27.ytinsight.YouTubeClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.io.File

suspend fun uploadFile(file: File): UploadFileResponse {
    return YouTubeClient.http.post("https://api.writer.com/v1/files") {
        header("Authorization", "Bearer ${Config.WRITER_AI_API_KEY}")
        header("Accept", "*/*")
        header("Content-Disposition", "attachment; filename=${file.name}")
        header("Content-Length", file.length().toString())
        header("Content-Type", "text/plain; charset=UTF-8")
        parameter("graphId", "7d1c0ea5-e920-48ea-b84c-5da32634901e")
        setBody(file.readBytes())
    }.body<UploadFileResponse>()
}