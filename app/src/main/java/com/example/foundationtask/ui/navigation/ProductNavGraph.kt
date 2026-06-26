package com.example.foundationtask.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.foundationtask.ui.details.ProductDetailsScreen
import com.example.foundationtask.ui.details.ProductDetailsSideEffect
import com.example.foundationtask.ui.details.ProductDetailsUiEvent
import com.example.foundationtask.ui.details.ProductDetailsViewModel
import com.example.foundationtask.ui.list.ProductListSideEffect
import com.example.foundationtask.ui.list.ProductListUiEvent
import com.example.foundationtask.ui.list.ProductListViewModel
import com.example.foundationtask.ui.list.ProductsListScreen
import com.example.foundationtask.ui.utils.ObserveAsEvents

@Composable
fun ProductsNavHost() {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ProductRoute.ProductList
    ) {
        productListScreen(navController)
        productDetailsScreen(navController)
    }
}

fun NavGraphBuilder.productListScreen(
    navController: NavHostController
) {
    composable<ProductRoute.ProductList> {
        val viewModel: ProductListViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.onEvent(ProductListUiEvent.LoadProducts(false))
        }

        ObserveAsEvents(viewModel.sideEffect) { event ->
            when (event) {
                is ProductListSideEffect.NavigateToDetails -> {
                    navController.navigate(
                        ProductRoute.ProductDetails(event.id)
                    )
                }
            }
        }

        ProductsListScreen(
            state = uiState,
            onEvent = viewModel::onEvent
        )
    }
}

fun NavGraphBuilder.productDetailsScreen(
    navController: NavHostController
) {
    composable<ProductRoute.ProductDetails> { backStackEntry ->
        val route: ProductRoute.ProductDetails = backStackEntry.toRoute()
        val productId = route.id
        val viewModel: ProductDetailsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.onEvent(ProductDetailsUiEvent.LoadProductById(productId))
        }

        ObserveAsEvents(viewModel.sideEffect) { event ->
            when (event) {
                is ProductDetailsSideEffect.PopBackStack -> {
                    navController.popBackStack()
                }
            }
        }

        ProductDetailsScreen(
            state = uiState,
            onEvent = viewModel::onEvent
        )
    }
}