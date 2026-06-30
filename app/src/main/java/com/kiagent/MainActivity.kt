package com.kiagent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kiagent.config.AgentPreferences
import com.kiagent.config.Provider
import com.kiagent.model.ChatMessage
import com.kiagent.viewmodel.AgentViewModel
import com.kiagent.viewmodel.AgentViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = AgentPreferences(applicationContext)

        setContent {
            val vm: AgentViewModel = viewModel(
                factory = AgentViewModelFactory(preferences)
            )
            AgentScreen(vm)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentScreen(vm: AgentViewModel) {
    val messages by vm.messages.collectAsState()
    val config by vm.config.collectAsState()
    val busy by vm.busy.collectAsState()
    var input by remember { mutableStateOf("") }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { vm.setLocalModelPath(it.toString()) }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("KI Agent") })
            }
        ) { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Backend", style = MaterialTheme.typography.titleMedium)

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(
                                    Provider.LOCAL,
                                    Provider.OPENAI,
                                    Provider.OPENROUTER,
                                    Provider.GEMINI,
                                    Provider.CUSTOM
                                ).forEach { provider ->
                                    FilterChip(
                                        selected = config.provider == provider,
                                        onClick = { vm.setProvider(provider) },
                                        label = { Text(provider.name) }
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = config.apiKey,
                                onValueChange = vm::setApiKey,
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("API-Key") },
                                visualTransformation = PasswordVisualTransformation()
                            )

                            OutlinedTextField(
                                value = config.endpoint,
                                onValueChange = vm::setEndpoint,
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Endpoint") }
                            )

                            OutlinedTextField(
                                value = config.model,
                                onValueChange = vm::setModel,
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Modell") }
                            )

                            Button(onClick = { picker.launch(arrayOf("*/*")) }) {
                                Text("Lokales Modell wählen")
                            }

                            Text(
                                text = if (config.localModelPath.isBlank()) {
                                    "Kein lokales Modell gewählt"
                                } else {
                                    "Lokales Modell: ${config.localModelPath}"
                                },
                                style = MaterialTheme.typography.bodySmall
                            )

                            Text(
                                text = "Aktiver Modus: ${config.provider}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(messages) { msg ->
                                MessageBubble(msg)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nachricht") },
                        enabled = !busy,
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            vm.send(input)
                            input = ""
                        },
                        enabled = !busy,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (busy) "..." else "Senden")
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val title = if (message.role == "user") "Du" else "Agent"

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            Text(message.content)
        }
    }
}
