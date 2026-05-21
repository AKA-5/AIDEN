package com.muhammadkaleemakhtar.aiden.ui.screens

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammadkaleemakhtar.aiden.agents.AgentLogger
import com.muhammadkaleemakhtar.aiden.ui.components.AgentLogViewer
import com.muhammadkaleemakhtar.aiden.ui.components.MetricCard
import com.muhammadkaleemakhtar.aiden.ui.viewmodel.AidenViewModel
import com.muhammadkaleemakhtar.aiden.utils.FileExporter
import com.muhammadkaleemakhtar.aiden.utils.ReportSharer
import com.muhammadkaleemakhtar.aiden.utils.DiagnosticTest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutcomeScreen(
    viewModel: AidenViewModel,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentCrisisState by viewModel.currentCrisisState.collectAsState()
    val agentLogs by viewModel.agentLogs.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0 = Before, 1 = After
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()
    var showDiagnosticDialog by remember { mutableStateOf(false) }
    var diagnosticResultText by remember { mutableStateOf("") }
    var isRunningDiagnostic by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Impact Analysis", style = MaterialTheme.typography.headlineMedium) },
                actions = {
                    IconButton(onClick = {
                        val json = AgentLogger.exportToJson()
                        val uri = FileExporter.exportLogsToDownloads(context, json)
                        if (uri != null) {
                            Toast.makeText(context, "Logs exported to Downloads folder!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Failed to export logs", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Outlined.Info, contentDescription = "Export Logs")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(verticalScrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tab Toggle Before vs After
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Before Response", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("After Response", fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Map Mockup Canvas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Crossfade(targetState = selectedTab, label = "MapCrossfade") { tab ->
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE8DCD4)) // Warm sand background
                    ) {
                        val width = size.width
                        val height = size.height

                        // Draw Grid Lines representing streets of G-10 Sector
                        val gridCount = 5
                        for (i in 1..gridCount) {
                            val x = width * (i.toFloat() / (gridCount + 1))
                            drawLine(
                                color = Color.White,
                                start = Offset(x, 0f),
                                end = Offset(x, height),
                                strokeWidth = 8f
                            )
                            val y = height * (i.toFloat() / (gridCount + 1))
                            drawLine(
                                color = Color.White,
                                start = Offset(0f, y),
                                end = Offset(width, y),
                                strokeWidth = 8f
                            )
                        }

                        // Draw Markers based on Tab state
                        if (tab == 0) {
                            // Before: Draw RED circular blockages at road intersections
                            drawCircle(
                                color = Color(0xFFE63946), // Crisis Red
                                radius = 20f,
                                center = Offset(width * 0.33f, height * 0.33f)
                            )
                            drawCircle(
                                color = Color(0xFFE63946),
                                radius = 20f,
                                center = Offset(width * 0.66f, height * 0.66f)
                            )
                        } else {
                            // After: Draw GREEN circular clearance indicators
                            drawCircle(
                                color = Color(0xFF2A9D8F), // Success Teal Green
                                radius = 20f,
                                center = Offset(width * 0.33f, height * 0.33f)
                            )
                            drawCircle(
                                color = Color(0xFF2A9D8F),
                                radius = 20f,
                                center = Offset(width * 0.66f, height * 0.66f)
                            )
                            // Draw emergency vehicle dots
                            drawCircle(
                                color = Color(0xFFE76F51), // Terracotta rescue units
                                radius = 12f,
                                center = Offset(width * 0.33f + 40f, height * 0.33f)
                            )
                            drawCircle(
                                color = Color(0xFFE76F51),
                                radius = 12f,
                                center = Offset(width * 0.66f - 40f, height * 0.66f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Horizontally Scrollable Metrics Cards Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (selectedTab == 0) {
                    MetricCard(
                        title = "Stranded Vehicles",
                        value = "45",
                        icon = Icons.Outlined.Warning,
                        subValue = "Initial Blockage"
                    )
                    MetricCard(
                        title = "Avg Response Time",
                        value = "45 mins",
                        icon = Icons.Outlined.Warning,
                        subValue = "Congested Routes"
                    )
                    MetricCard(
                        title = "Rescue Teams",
                        value = "0 Deployed",
                        icon = Icons.Outlined.Warning,
                        subValue = "Awaiting Orders"
                    )
                } else {
                    MetricCard(
                        title = "Stranded Vehicles",
                        value = "${currentCrisisState.strandedVehicles}",
                        icon = Icons.Outlined.Check,
                        subValue = if (currentCrisisState.strandedVehicles == 0) "100% Cleared" else "Resolving"
                    )
                    MetricCard(
                        title = "Avg Response Time",
                        value = "${currentCrisisState.responseTimeMin} mins",
                        icon = Icons.Outlined.Check,
                        subValue = "Reduced by ${(100 - (currentCrisisState.responseTimeMin * 100f / 45)).toInt()}%"
                    )
                    MetricCard(
                        title = "Rescue Teams",
                        value = "${currentCrisisState.teamsDispatched} Deployed",
                        icon = Icons.Outlined.Check,
                        subValue = "${currentCrisisState.alertsSent} Alerts Sent"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Agent Log Viewer Panel
            AgentLogViewer(logs = agentLogs)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isRunningDiagnostic = true
                    coroutineScope.launch {
                        val result = DiagnosticTest.runAllTests(context)
                        diagnosticResultText = result
                        isRunningDiagnostic = false
                        showDiagnosticDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = !isRunningDiagnostic
            ) {
                if (isRunningDiagnostic) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Run Diagnostic Test", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        ReportSharer.shareReport(context)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Outlined.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Report", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        viewModel.reset()
                        onReset()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Outlined.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Report", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showDiagnosticDialog) {
        AlertDialog(
            onDismissRequest = { showDiagnosticDialog = false },
            title = { Text("Diagnostic Results", style = MaterialTheme.typography.titleLarge) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = diagnosticResultText,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDiagnosticDialog = false }) {
                    Text("Dismiss")
                }
            }
        )
    }
}
