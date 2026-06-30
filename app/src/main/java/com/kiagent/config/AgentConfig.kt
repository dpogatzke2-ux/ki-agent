package com.kiagent.config

enum class Provider {

    LOCAL,

    OPENAI,

    OPENROUTER,

    GEMINI,

    CUSTOM

}

data class AgentConfig(

    var provider: Provider = Provider.LOCAL,

    var apiKey: String = "",

    var endpoint: String = "",

    var model: String = "",

    var localModelPath: String = ""

)
