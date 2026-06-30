package com.kiagent.runtime

import com.kiagent.model.ChatMessage
import com.kiagent.model.LocalModel
import com.kiagent.model.ModelFormat

class MnnRuntime : ModelRuntime {

    private var model: LocalModel? = null

    override val format = ModelFormat.MNN

    override fun supports(model: LocalModel) = model.format == ModelFormat.MNN

    override suspend fun load(model: LocalModel): Boolean {
        this.model = model
        return true
    }

    override suspend fun unload() {
        model = null
    }

    override suspend fun generate(history: List<ChatMessage>): String {
        return "MNN Runtime (Phase 3.5)"
    }

    override fun isLoaded() = model != null
}
