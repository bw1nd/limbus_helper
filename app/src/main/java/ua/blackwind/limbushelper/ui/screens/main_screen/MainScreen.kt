package ua.blackwind.limbushelper.ui.screens.main_screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.ui.screens.NavGraphs
import ua.blackwind.limbushelper.ui.screens.destinations.DirectionDestination
import ua.blackwind.limbushelper.ui.screens.destinations.FilterScreenDestination
import ua.blackwind.limbushelper.ui.screens.destinations.PartyBuildingScreenDestination
import ua.blackwind.limbushelper.ui.screens.party_building_screen.PartyBuildingScreen

enum class LimbusBottomItem(
    val direction: DirectionDestination,
    val imageIcon: ImageVector,
    @StringRes val title: Int,
) {
    PartyItem(PartyBuildingScreenDestination, Icons.Default.Person, R.string.party),
    FilterItem(FilterScreenDestination, Icons.Default.List, R.string.filter)
}

@Composable
fun MainScreen(navController: NavHostController) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .height(80.dp)
                    .navigationBarsPadding()
            ) {
                LimbusBottomItem.values().forEach { destination ->
                    val currentDestination by navController.currentDestinationAsState()
                    val isItemSelected =
                        currentDestination?.startDestination == destination.direction
                    NavigationBarItem(selected = isItemSelected, onClick = {
                        navController.navigate(destination.direction) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                        icon = {
                            Icon(imageVector = destination.imageIcon, contentDescription = null)
                        },
                        label = {
                            val fontWeight =
                                if (isItemSelected) FontWeight.Bold else FontWeight.Normal
                            Text(
                                text = stringResource(id = destination.title),
                                fontWeight = fontWeight
                            )
                        }
                    )
                }
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