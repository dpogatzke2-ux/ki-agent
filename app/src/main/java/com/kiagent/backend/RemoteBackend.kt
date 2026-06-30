package com.kiagent.backend

import com.kiagent.config.AgentConfig
import com.kiagent.model.ChatMessage
import com.kiagent.network.HttpLlmClient
import com.kiagent.network.LlmRequest

class RemoteBackend(
    private val configProvider: () -> AgentConfig = { AgentConfig() },
    private val client: HttpLlmClient = HttpLlmClient()
) : ModelBackend {

    override val name: String = "Remote"

    override suspend fun initialize() {}

    override suspend fun shutdown() {}

    override suspend fun generate(messages: List<ChatMessage>): String {
        val config = configProvider()

        if (config.endpoint.isBlank()) {
            return "Kein Endpoint gesetzt."
        }

        if (config.apiKey.isBlank()) {
            return "Kein API-Key gesetzt."
        }

        val request = LlmRequest(
            model = config.model.ifBlank { "gpt-4o-mini" },
            messages = messages,
            temperature = 0.7f,
            stream = false
        )

        return client.complete(
            endpoint = config.endpoint,
            apiKey = config.apiKey,
            request = request
        )
    }

    override fun isAvailable(): Boolean = true
}
