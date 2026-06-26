package com.example.foundationtask.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foundationtask.domain.exception.DomainException
import com.example.foundationtask.domain.mapper.toUiModel
import com.example.foundationtask.domain.usecase.GetProductByIdUseCase
import com.example.foundationtask.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ProductDetailsSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ProductDetailsSideEffect> = _sideEffect.receiveAsFlow()

    fun onEvent(event: ProductDetailsUiEvent) {
        when (event) {
            is ProductDetailsUiEvent.LoadProductById -> loadProductById(event.productId)
            is ProductDetailsUiEvent.OnClickBackIcon -> popBackStack()
            is ProductDetailsUiEvent.OnFavoriteClicked -> toggleFavorite(event.productId)
        }
    }

    private fun loadProductById(productId: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailsUiState.Loading
            try {
                getProductByIdUseCase(id = productId)?.let {
                    _uiState.value = ProductDetailsUiState.Success(it.toUiModel())
                }
            } catch (e: DomainException) {
                _uiState.value = ProductDetailsUiState.Failure(
                    productId = productId,
                    exception = e
                )
            }
        }
    }

    private fun toggleFavorite(productId: Int) {
        _uiState.update { state ->
            if (state is ProductDetailsUiState.Success) {
                state.copy(
                    product = state.product.copy(
                        isFavorite = !state.product.isFavorite
                    )
                )
            } else {
                state
            }
        }
        viewModelScope.launch {
            toggleFavoriteUseCase(productId)
        }
    }

    private fun popBackStack() {
        viewModelScope.launch {
            _sideEffect.send(ProductDetailsSideEffect.PopBackStack)
        }
    }
}