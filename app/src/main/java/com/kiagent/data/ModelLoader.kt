package com.kiagent.data

import com.kiagent.model.ModelFormat

object ModelLoader {

    fun detectFormat(path: String): ModelFormat {

        val p = path.lowercase()

        return when {

            p.endsWith(".gguf") ->
                ModelFormat.GGUF

            p.endsWith(".mnn") ->
                ModelFormat.MNN

            p.endsWith(".onnx") ->
                ModelFormat.ONNX

            p.endsWith(".tflite") ->
                ModelFormat.TFLITE

            p.endsWith(".safetensors") ->
                ModelFormat.SAFETENSORS

            else ->
                ModelFormat.UNKNOWN
        }
    }
}
