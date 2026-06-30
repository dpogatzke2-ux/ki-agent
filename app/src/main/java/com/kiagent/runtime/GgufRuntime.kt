package com.kiagent.runtime

import com.kiagent.model.ChatMessage
import com.kiagent.model.LocalModel
import com.kiagent.model.ModelFormat

class GgufRuntime : ModelRuntime {

    private var currentModel: LocalModel? = null

    override val format = ModelFormat.GGUF

    override fun supports(model: LocalModel): Boolean {
        return model.format == ModelFormat.GGUF
    }

    override suspend fun load(model: LocalModel): Boolean {
        currentModel = model
        return true
    }

    override suspend fun unload() {
        currentModel = null
    }

    override suspend fun generate(
        history: List<ChatMessage>
    ): String {

        val model = currentModel
            ?: return "Kein GGUF-Modell geladen."

        return """
GGUF Runtime

Modell:
${model.name}

Inference folgt
in Phase 3.4
(llama.cpp)
""".trimIndent()

    }

    override fun isLoaded(): Boolean {
        return currentModel != null
    }

}
