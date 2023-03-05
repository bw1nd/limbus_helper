package ua.blackwind.limbushelper.ui.screens.main_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import ua.blackwind.limbushelper.ui.screens.NavGraphs
import ua.blackwind.limbushelper.ui.screens.destinations.FilterScreenDestination
import ua.blackwind.limbushelper.ui.screens.destinations.PartyBuilderScreenDestination


@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomAppBar() {
                NavigationBarItem(selected = false,
                    onClick = {
                        if (navController.currentDestination?.route != PartyBuilderScreenDestination.route) {
                            navController.navigate(PartyBuilderScreenDestination)
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
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                navController = navController
            )
        }
    }
}

@Preview
@Composable
fun mainScreenPreview() {
    //MainScreen(NavHostController())
}