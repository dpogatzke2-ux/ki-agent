package com.kiagent.backend

class BackendRouter {

    private var backend: ModelBackend = DummyBackend()

    fun setBackend(newBackend: ModelBackend) {
        backend = newBackend
    }

    fun currentBackend(): ModelBackend {
        return backend
    }

    suspend fun generate(prompt: String): String {
        return backend.generate(
            listOf(
                com.kiagent.model.ChatMessage(
                    role = "user",
                    content = prompt
                )
            )
        )
    }
}
