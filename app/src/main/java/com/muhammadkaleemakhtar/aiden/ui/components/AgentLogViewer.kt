package com.muhammadkaleemakhtar.aiden.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammadkaleemakhtar.aiden.agents.AgentLogger
import com.muhammadkaleemakhtar.aiden.ui.theme.JetBrainsMonoFontFamily

@Composable
fun AgentLogViewer(
    logs: List<AgentLogger.LogEntry>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF231C18), // Deep warm brown terminal
            contentColor = Color(0xFFFDF8F5)
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "AGENT TRACE CONSOLE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontFamily = JetBrainsMonoFontFamily,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Console inactive. Submit reports to initiate.",
                        fontFamily = JetBrainsMonoFontFamily,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    items(logs) { entry ->
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = "[AGENT: ${entry.agent.uppercase()}]",
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = when (entry.agent) {
                                    "Sentinel" -> Color(0xFFE76F51) // Terracotta
                                    "Planner" -> Color(0xFFF4A261)  // Sand
                                    "Executor" -> Color(0xFF2A9D8F) // Teal
                                    else -> Color.Gray
                                }
                            )
                            Text(
                                text = "Reasoning: ${entry.reasoning}",
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFE8DCD4),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 6.dp),
                                color = Color(0xFF7F5E4A).copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}
