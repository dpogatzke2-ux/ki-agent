package com.kiagent.backend

import com.kiagent.model.ChatMessage

interface ModelBackend {

    val name: String

    suspend fun initialize()

    suspend fun shutdown()

    suspend fun generate(
        messages: List<ChatMessage>
    ): String

    fun isAvailable(): Boolean
}
