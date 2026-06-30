package com.kiagent.backend

import com.kiagent.model.ChatMessage

class DummyBackend : ModelBackend {

    override val name = "Dummy"

    override suspend fun initialize() {}

    override suspend fun shutdown() {}

    override suspend fun generate(
        messages: List<ChatMessage>
    ): String {

        val last =
            messages.lastOrNull()?.content ?: ""

        return """
Dummy Backend

Du hast geschrieben:

$last

Backend-System funktioniert.
        """.trimIndent()
    }

    override fun isAvailable() = true
}
