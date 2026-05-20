package com.muhammadkaleemakhtar.aiden.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.muhammadkaleemakhtar.aiden.ui.screens.ActionScreen
import com.muhammadkaleemakhtar.aiden.ui.screens.DetectionScreen
import com.muhammadkaleemakhtar.aiden.ui.screens.InputScreen
import com.muhammadkaleemakhtar.aiden.ui.screens.OutcomeScreen
import com.muhammadkaleemakhtar.aiden.ui.viewmodel.AidenViewModel

sealed class Screen(val route: String) {
    object Input : Screen("input")
    object Detection : Screen("detection")
    object Action : Screen("action")
    object Outcome : Screen("outcome")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: AidenViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Input.route,
        modifier = modifier
    ) {
        composable(Screen.Input.route) {
            InputScreen(
                viewModel = viewModel,
                onNavigateToDetection = {
                    navController.navigate(Screen.Detection.route)
                }
            )
        }
        composable(Screen.Detection.route) {
            DetectionScreen(
                viewModel = viewModel,
                onNavigateToAction = {
                    val detection = viewModel.detectionResult.value
                    if (detection != null && detection.isCrisis) {
                        navController.navigate(Screen.Action.route)
                    } else {
                        navController.navigate(Screen.Outcome.route)
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Action.route) {
            ActionScreen(
                viewModel = viewModel,
                onNavigateToOutcome = {
                    navController.navigate(Screen.Outcome.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Outcome.route) {
            OutcomeScreen(
                viewModel = viewModel,
                onReset = {
                    navController.navigate(Screen.Input.route) {
                        popUpTo(Screen.Input.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
