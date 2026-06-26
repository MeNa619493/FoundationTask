package com.example.foundationtask.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foundationtask.domain.exception.DomainException
import com.example.foundationtask.domain.mapper.toUiModel
import com.example.foundationtask.domain.usecase.GetProductsUseCase
import com.example.foundationtask.domain.usecase.SearchProductsUseCase
import com.example.foundationtask.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ProductListSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ProductListSideEffect> = _sideEffect.receiveAsFlow()

    private var searchJob: Job? = null

    fun onEvent(event: ProductListUiEvent) {
        when (event) {
            is ProductListUiEvent.LoadProducts -> loadProducts(event.forceRefresh)
            is ProductListUiEvent.OnProductClicked -> navigateToDetails(event.id)
            is ProductListUiEvent.OnFavoriteClicked -> toggleFavorite(event.productId)
            is ProductListUiEvent.OnQueryChanged -> searchProducts(event.query)
        }
    }

    private fun loadProducts(forceRefresh: Boolean) {
        searchJob?.cancel()
        viewModelScope.launch {
            _uiState.value = ProductListUiState.Loading
            getProductsUseCase(forceRefresh = forceRefresh)
                .catch { e ->
                    _uiState.value = ProductListUiState.Failure(
                        exception = e as? DomainException
                    )
                }
                .collect { products ->
                    val products = products?.map { it.toUiModel() } ?: emptyList()
                    _uiState.value = ProductListUiState.Success(
                        products = products
                    )
                }
        }
    }

    private fun searchProducts(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (_uiState.value !is ProductListUiState.Success) return@launch
            val products = searchProductsUseCase(query)
            _uiState.value = ProductListUiState.Success(
                products = products?.map { it.toUiModel() } ?: emptyList(),
                searchQuery = query.takeIf { it.isNotBlank() }
            )
        }
    }

    private fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(productId)
        }
    }

    private fun navigateToDetails(id: Int) {
        viewModelScope.launch {
            _sideEffect.send(ProductListSideEffect.NavigateToDetails(id))
        }
    }
}