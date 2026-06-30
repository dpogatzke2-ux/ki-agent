package com.kiagent.runtime

import com.kiagent.model.ChatMessage
import com.kiagent.model.LocalModel
import com.kiagent.model.ModelFormat

interface ModelRuntime {

    val format: ModelFormat

    fun supports(model: LocalModel): Boolean

    suspend fun load(model: LocalModel): Boolean

    suspend fun unload()

    suspend fun generate(
        history: List<ChatMessage>
    ): String

    fun isLoaded(): Boolean

}
