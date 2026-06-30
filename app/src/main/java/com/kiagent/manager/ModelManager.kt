package com.kiagent.manager

import com.kiagent.data.ModelLoader
import com.kiagent.model.ModelFormat
import com.kiagent.model.ModelInfo
import com.kiagent.model.ModelRegistry
import java.io.File
import java.util.UUID

object ModelManager {

    fun scanDirectory(directory: File): List<ModelInfo> {
        if (!directory.exists() || !directory.isDirectory) return emptyList()

        val found = mutableListOf<ModelInfo>()

        directory.walkTopDown().forEach { file ->
            if (!file.isFile) return@forEach

            val format = ModelLoader.detectFormat(file)
            if (format == ModelFormat.UNKNOWN) return@forEach

            val info = createInfo(file, format)
            ModelRegistry.register(info)
            found.add(info)
        }

        return found
    }

    fun scanDirectories(directories: List<File>): List<ModelInfo> {
        val all = mutableListOf<ModelInfo>()

        directories.forEach { dir ->
            all += scanDirectory(dir)
        }

        return all.distinctBy { it.id }
    }

    fun allRegistered(): List<ModelInfo> = ModelRegistry.all()

    fun clear() {
        ModelRegistry.clear()
    }

    private fun createInfo(file: File, format: ModelFormat): ModelInfo {
        val sizeMb = (file.length() / (1024 * 1024)).toInt().coerceAtLeast(1)

        val supportsVision = when (format) {
            ModelFormat.ONNX,
            ModelFormat.LITERT -> false
            else -> false
        }

        val supportsTools = when (format) {
            ModelFormat.GGUF,
            ModelFormat.MNN,
            ModelFormat.ONNX,
            ModelFormat.LITERT,
            ModelFormat.EXECUTORCH -> true
            else -> false
        }

        val supportsReasoning = when (format) {
            ModelFormat.GGUF,
            ModelFormat.MNN,
            ModelFormat.ONNX,
            ModelFormat.LITERT,
            ModelFormat.EXECUTORCH -> true
            else -> false
        }

        val contextSize = when (format) {
            ModelFormat.GGUF -> 8192
            ModelFormat.SAFETENSORS -> 8192
            ModelFormat.MNN -> 4096
            ModelFormat.ONNX -> 4096
            ModelFormat.LITERT -> 4096
            ModelFormat.EXECUTORCH -> 4096
            else -> 2048
        }

        val estimatedRamMb = when (format) {
            ModelFormat.GGUF -> (sizeMb * 1.5).toInt()
            ModelFormat.SAFETENSORS -> (sizeMb * 2.0).toInt()
            ModelFormat.MNN -> (sizeMb * 1.2).toInt()
            ModelFormat.ONNX -> (sizeMb * 1.2).toInt()
            ModelFormat.LITERT -> (sizeMb * 1.1).toInt()
            ModelFormat.EXECUTORCH -> (sizeMb * 1.1).toInt()
            else -> sizeMb
        }.coerceAtLeast(128)

        return ModelInfo(
            id = UUID.nameUUIDFromBytes(file.absolutePath.toByteArray()).toString(),
            displayName = file.nameWithoutExtension.ifBlank { file.name },
            format = format,
            local = true,
            supportsVision = supportsVision,
            supportsTools = supportsTools,
            supportsReasoning = supportsReasoning,
            contextSize = contextSize,
            estimatedRamMb = estimatedRamMb
        )
    }
}
