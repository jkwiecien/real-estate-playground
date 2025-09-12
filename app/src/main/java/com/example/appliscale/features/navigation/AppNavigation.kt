package com.example.appliscale.features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appliscale.features.details.DetailsScreen
import com.example.appliscale.features.details.DetailsViewModel
import com.example.appliscale.features.list.ListScreen
import com.example.appliscale.features.list.ListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.LIST) {
        composable(Route.LIST) {
            val viewModel = koinViewModel<ListViewModel>()
            ListScreen(
                viewModel = viewModel,
                onItemClick = { listingId ->
                    navController.navigate("details/$listingId")
                }
            )
        }
        composable(
            route = Route.DETAILS,
            arguments = listOf(navArgument("listingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listingId = backStackEntry.arguments?.getInt("listingId") ?: -1
            val viewModel = koinViewModel<DetailsViewModel>(parameters = { parametersOf(listingId) })
            DetailsScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}