package com.kiagent.agent

import com.kiagent.backend.BackendRouter
import com.kiagent.model.ChatMessage

class AgentEngine(
    private val router: BackendRouter
) {

    suspend fun ask(
        history: List<ChatMessage>
    ): String {

        return router.generate(history)

    }

}
