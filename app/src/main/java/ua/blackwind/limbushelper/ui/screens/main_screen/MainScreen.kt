package ua.blackwind.limbushelper.ui.screens.main_screen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.navigate
import ua.blackwind.limbushelper.ui.screens.NavGraphs
import ua.blackwind.limbushelper.ui.screens.destinations.FilterScreenDestination
import ua.blackwind.limbushelper.ui.screens.destinations.PartyBuildingScreenDestination
import ua.blackwind.limbushelper.ui.screens.party_building_screen.PartyBuildingScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(80.dp)) {
                NavigationBarItem(selected = false,
                    onClick = {
                        if (navController.currentDestination?.route != PartyBuildingScreenDestination.route) {
                            navController.navigate(PartyBuildingScreenDestination)
                        }
                    },
                    icon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                    label = { Text("Party") }
                )
                NavigationBarItem(selected = false,
                    onClick = {
                        if (navController.currentDestination?.route != FilterScreenDestination.route) {
                            navController.navigate(FilterScreenDestination)
                        }
                    },
                    icon = { Icon(imageVector = Icons.Default.List, contentDescription = null) },
                    label = { Text("Filter") }
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->

        val showSnackBar: suspend (String, String) -> SnackbarResult =
            { message: String, actionLabel: String ->
                snackBarHostState.showSnackbar(
                    message,
                    actionLabel,
                    withDismissAction = false,
                    duration = SnackbarDuration.Short
                )
            }
        Surface(modifier = Modifier.padding(padding)) {
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                navController = navController
            ) {
                composable(PartyBuildingScreenDestination) {
                    PartyBuildingScreen(showSnackBar)
                }
            }
        }
    }
}