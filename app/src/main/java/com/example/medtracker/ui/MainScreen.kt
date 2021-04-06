package com.example.medtracker.ui

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.medtracker.R
import com.example.medtracker.data.viewmodel.HeartRateViewModel


sealed class BottomNavigationScreens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object HR : BottomNavigationScreens("HR", R.string.hr_route, Icons.Filled.Home)
    object Run : BottomNavigationScreens("Run", R.string.run_route, Icons.Filled.VolunteerActivism)
    object Stats : BottomNavigationScreens("Stats", R.string.stats_route, Icons.Filled.History)
    object Account : BottomNavigationScreens("Account", R.string.account_route, Icons.Filled.Settings)
}

sealed class ScreenContent(val word: String){
    object HR: ScreenContent("Hello")
    object Run: ScreenContent("Hello 2")
    object Stats: ScreenContent("Hello 3")
    object Account: ScreenContent("Hello 4")
}

@Composable
fun MainScreen(heartRateViewModel: HeartRateViewModel) {

    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavigationScreens.HR,
        BottomNavigationScreens.Run,
        BottomNavigationScreens.Stats,
        BottomNavigationScreens.Account
    )
    Scaffold(
        bottomBar = {
            MedTrackerBottomNavigation(navController, bottomNavigationItems)
        },
    ) {
        MainScreenNavigationConfigurations(navController, heartRateViewModel = heartRateViewModel)
    }
}

@Composable
private fun MainScreenNavigationConfigurations(
    navController: NavHostController,
    heartRateViewModel: HeartRateViewModel
) {
    NavHost(navController, startDestination = BottomNavigationScreens.HR.route) {
        composable(BottomNavigationScreens.HR.route) {
//            MedTrackerScreen(ScreenContent.HR)
            HeartRateScreen()
        }
        composable(BottomNavigationScreens.Run.route) {
//            MedTrackerScreen(ScreenContent.Run)
            CityMapView(latitude = "51.075103", longitude = "16.992012" )
        }
        composable(BottomNavigationScreens.Stats.route) {
//            MedTrackerScreen(ScreenContent.Stats)
            StatsScreen(heartRateViewModel = heartRateViewModel)
        }
        composable(BottomNavigationScreens.Account.route) {
            MedTrackerScreen(ScreenContent.Account)
        }
    }
}

@Composable
fun MedTrackerScreen(
    screenContent: ScreenContent
) {
    Text(text = screenContent.word)
}

@Composable
private fun MedTrackerBottomNavigation(
    navController: NavHostController,
    items: List<BottomNavigationScreens>
) {
    BottomNavigation {
        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
//                label = { Text(stringResource(id = screen.resourceId)) },
                selected = currentRoute == screen.route,
                onClick = {
                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route)
                    }
                }
            )
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString(KEY_ROUTE)
}