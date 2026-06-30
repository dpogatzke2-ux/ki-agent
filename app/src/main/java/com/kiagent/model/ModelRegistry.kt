package com.kiagent.model

data class ModelInfo(
    val id: String,
    val displayName: String,
    val format: ModelFormat,
    val local: Boolean,
    val supportsVision: Boolean,
    val supportsTools: Boolean,
    val supportsReasoning: Boolean,
    val contextSize: Int,
    val estimatedRamMb: Int
)

object ModelRegistry {
    private val models = mutableListOf<ModelInfo>()

    fun register(model: ModelInfo) {
        models.removeAll { it.id == model.id }
        models.add(model)
    }

    fun all(): List<ModelInfo> = models.toList()

    fun get(id: String): ModelInfo? {
        return models.firstOrNull { it.id == id }
    }

    fun clear() {
        models.clear()
    }
}
