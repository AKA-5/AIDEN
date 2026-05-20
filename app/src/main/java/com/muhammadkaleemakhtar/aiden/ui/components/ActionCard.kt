package com.muhammadkaleemakhtar.aiden.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammadkaleemakhtar.aiden.data.models.Action
import com.muhammadkaleemakhtar.aiden.data.models.SimulationResult

@Composable
fun ActionCard(
    action: Action,
    simulationResult: SimulationResult?,
    onSimulate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (action.name) {
        "Traffic Rerouting" -> Icons.Outlined.Info
        "Public Alerts" -> Icons.Outlined.Notifications
        else -> Icons.Outlined.Warning
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = action.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MaterialTheme.typography.displayMedium.fontFamily
                )
                Spacer(modifier = Modifier.weight(1f))

                if (simulationResult != null) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = "Simulated",
                        tint = Color(0xFF2A9D8F),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = action.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    // Before State Pill
                    Surface(
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = when (action.name) {
                                "Traffic Rerouting" -> "Before: 2 blocked roads"
                                "Emergency Dispatch" -> "Before: 0 teams"
                                "Public Alerts" -> "Before: 0 alerts"
                                else -> "Before: Active"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // After State Pill
                    AnimatedVisibility(visible = simulationResult != null) {
                        Surface(
                            color = Color(0xFF2A9D8F).copy(alpha = 0.1f),
                            contentColor = Color(0xFF2A9D8F),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = when (action.name) {
                                    "Traffic Rerouting" -> "After: 0 blocked roads"
                                    "Emergency Dispatch" -> "After: 3 teams"
                                    "Public Alerts" -> "After: 245 alerts"
                                    else -> "After: Complete"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (simulationResult == null) {
                    Button(
                        onClick = onSimulate,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2A9D8F),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Simulate",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            AnimatedVisibility(visible = simulationResult != null) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = simulationResult?.message ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
