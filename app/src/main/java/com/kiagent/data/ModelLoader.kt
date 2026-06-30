package com.kiagent.data

import com.kiagent.model.ModelFormat
import java.io.File

object ModelLoader {

    fun detectFormat(fileName: String): ModelFormat {
        val name = fileName.lowercase()

        return when {
            name.endsWith(".gguf") -> ModelFormat.GGUF
            name.endsWith(".safetensors") -> ModelFormat.SAFETENSORS
            name.endsWith(".mnn") -> ModelFormat.MNN
            name.endsWith(".onnx") -> ModelFormat.ONNX
            name.endsWith(".tflite") -> ModelFormat.LITERT
            name.endsWith(".pte") -> ModelFormat.EXECUTORCH
            else -> ModelFormat.UNKNOWN
        }
    }

    fun detectFormat(file: File): ModelFormat = detectFormat(file.name)
}
