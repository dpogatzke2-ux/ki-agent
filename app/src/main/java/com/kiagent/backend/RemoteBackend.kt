package com.kiagent.backend

import com.kiagent.model.ChatMessage

class RemoteBackend : ModelBackend {

    override suspend fun generate(
        messages: List<ChatMessage>
    ): String {

        return """
Remote Backend

Noch nicht verbunden.

In Phase 2.1
verbinden wir OpenAI,
OpenRouter
und Gemini.
""".trimIndent()

    }
}
