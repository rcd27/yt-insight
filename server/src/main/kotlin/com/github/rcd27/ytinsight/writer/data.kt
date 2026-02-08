package com.github.rcd27.ytinsight.writer

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GetGraphsResponse(
    val data: List<GraphData>
)

@Serializable
data class GraphData(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    val name: String,
    val description: String,
    @SerialName("file_status")
    val fileStatus: FileStatus,
    val type: String,
    val urls: List<String>? = null
)

@Serializable
data class FileStatus(
    @SerialName("in_progress")
    val inProgress: Int,
    val completed: Int,
    val failed: Int,
    val total: Int
)

@Serializable
data class UploadFileResponse(
    val id: String,
    val name: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("graph_ids")
    val graphIds: List<String>,
    val status: String
)