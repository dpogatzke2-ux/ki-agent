package com.kiagent.network

import com.kiagent.model.ChatMessage

data class LlmRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Float = 0.7f,
    val stream: Boolean = false
)

data class LlmChoice(
    val message: ChatMessage
)

data class LlmResponse(
    val choices: List<LlmChoice>
)
