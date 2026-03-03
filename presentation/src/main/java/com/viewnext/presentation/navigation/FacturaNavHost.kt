package com.viewnext.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viewnext.presentation.composables.factura.FacturaScreen
import com.viewnext.presentation.composables.factura.FilterScreenFull
import com.viewnext.presentation.viewmodel.FacturaViewModel

@Composable
fun FacturaNavHost(
    viewModel: FacturaViewModel,
    usingRetromock: Boolean,
    onFinishActivity: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = FacturaRoutes.LIST
    ) {
        composable(FacturaRoutes.LIST) {
            FacturaScreen(
                viewModel = viewModel,
                usingRetromock = usingRetromock,
                onBack = onFinishActivity,
                onOpenFilters = {
                    navController.navigate(FacturaRoutes.FILTERS)
                }
            )
        }

        composable(FacturaRoutes.FILTERS) {
            FilterScreenFull(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
