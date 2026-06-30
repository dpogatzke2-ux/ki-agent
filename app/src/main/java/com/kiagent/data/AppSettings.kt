package com.kiagent.data

import com.kiagent.model.BackendMode

data class AppSettings(
    val backend: BackendMode = BackendMode.AUTO,

    // API
    val apiUrl: String = "",
    val apiKey: String = "",
    val apiModel: String = "",

    // Lokales Modell
    val localModelPath: String = "",
    val localModelName: String = "",

    // Zukunft
    val temperature: Float = 0.7f,
    val maxTokens: Int = 2048
)
