package com.kiagent.model

data class ChatMessage(
    val role: String,
    val content: String
)

data class LocalModel(
    val name: String,
    val uri: String,
    val format: ModelFormat
)
