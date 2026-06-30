package com.kiagent.model

enum class BackendMode {
    AUTO,
    LOCAL,
    REMOTE
}

data class ChatMessage(
    val role: String,
    val content: String
)

data class LocalModel(
    val name: String,
    val uri: String,
    val format: ModelFormat
)
