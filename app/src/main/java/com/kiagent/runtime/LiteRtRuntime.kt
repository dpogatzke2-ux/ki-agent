package com.kiagent.runtime

import com.kiagent.model.ChatMessage
import com.kiagent.model.LocalModel
import com.kiagent.model.ModelFormat

class LiteRtRuntime : ModelRuntime {

    private var model: LocalModel? = null

    override val format = ModelFormat.LITERT

    override fun supports(model: LocalModel) = model.format == ModelFormat.LITERT

    override suspend fun load(model: LocalModel): Boolean {
        this.model = model
        return true
    }

    override suspend fun unload() {
        model = null
    }

    override suspend fun generate(history: List<ChatMessage>): String {
        return "LiteRT Runtime (Phase 3.7)"
    }

    override fun isLoaded() = model != null
}
