package com.kiagent.backend

import android.net.Uri
import com.kiagent.config.AgentConfig
import com.kiagent.model.ChatMessage
import com.kiagent.scanner.ModelScanner
import java.io.File

class LocalBackend(
    private val configProvider: () -> AgentConfig = { AgentConfig() }
) : ModelBackend {

    override val name: String = "Local"

    override suspend fun initialize() {}

    override suspend fun shutdown() {}

    override suspend fun generate(messages: List<ChatMessage>): String {
        val config = configProvider()
        val path = config.localModelPath.trim()

        if (path.isBlank()) {
            return "Kein lokales Modell gewählt."
        }

        val displayName = when {
            path.startsWith("content://") || path.startsWith("file://") ->
                Uri.parse(path).lastPathSegment ?: path.substringAfterLast('/')

            else ->
                path.substringAfterLast('/')
        }

        val format = ModelScanner.detectFormat(File(displayName))

        return """
Lokales Modell registriert.

Name: $displayName
Format: $format
Pfad/URI: $path

Die eigentliche Inferenz folgt in Phase 3.
""".trimIndent()
    }

    override fun isAvailable(): Boolean = true
}
