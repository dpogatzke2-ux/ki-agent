package com.kiagent.backend

import com.kiagent.config.AgentConfig
import com.kiagent.config.Provider
import com.kiagent.model.ChatMessage

class BackendRouter(
    private val configProvider: () -> AgentConfig = { AgentConfig() }
) {

    private val localBackend = LocalBackend(configProvider)
    private val remoteBackend = RemoteBackend(configProvider)

    fun currentBackend(): ModelBackend {
        return when (configProvider().provider) {
            Provider.LOCAL -> localBackend
            Provider.OPENAI,
            Provider.OPENROUTER,
            Provider.GEMINI,
            Provider.CUSTOM -> remoteBackend
        }
    }

    suspend fun generate(messages: List<ChatMessage>): String {
        return currentBackend().generate(messages)
    }
}
