package com.kiagent.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class HttpLlmClient(
    private val client: OkHttpClient = OkHttpClient()
) {

    suspend fun complete(
        endpoint: String,
        apiKey: String,
        request: LlmRequest
    ): String = withContext(Dispatchers.IO) {

        val payload = JSONObject().apply {
            put("model", request.model)
            put("messages", JSONArray().apply {
                request.messages.forEach { message ->
                    put(JSONObject().apply {
                        put("role", message.role)
                        put("content", message.content)
                    })
                }
            })
            put("temperature", request.temperature)
            put("stream", request.stream)
        }

        val httpRequest = Request.Builder()
            .url(endpoint)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(payload.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(httpRequest).execute().use { response ->
            val body = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext "API-Fehler ${response.code}: $body"
            }

            return@withContext runCatching {
                val json = JSONObject(body)
                json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
            }.getOrElse {
                "Antwort konnte nicht gelesen werden: $body"
            }
        }
    }
}
