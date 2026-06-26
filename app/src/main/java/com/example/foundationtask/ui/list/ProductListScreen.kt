package com.example.foundationtask.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.foundationtask.ui.list.ProductListUiEvent.OnQueryChanged
import com.example.foundationtask.ui.list.ProductListUiEvent.LoadProducts
import com.example.foundationtask.ui.list.ProductListUiEvent.OnFavoriteClicked
import com.example.foundationtask.ui.list.ProductListUiEvent.OnProductClicked
import com.example.foundationtask.ui.localization.LocalFeatureLocalization
import com.example.foundationtask.ui.model.ProductItemUiModel
import com.example.foundationtask.ui.theme.FoundationTaskTheme
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ProductsListScreen(
    state: ProductListUiState,
    onEvent: (ProductListUiEvent) -> Unit
) {
    val localization = LocalFeatureLocalization.current
    val listState = rememberLazyListState()
    Scaffold { innerPadding ->
        when (state) {
            ProductListUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .wrapContentSize()
                    )
                }
            }

            is ProductListUiState.Failure -> {
                ProductsErrorState(
                    onRetry = {
                        onEvent(LoadProducts(true))
                    },
                    title = localization.errorTitle(state.exception),
                    message = localization.errorMessage(state.exception)
                )
            }

            is ProductListUiState.Success -> {
                LaunchedEffect(state.searchQuery) {
                    listState.animateScrollToItem(0)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    ProductSearchBar(
                        query = state.searchQuery ?: "",
                        onQueryChange = { onEvent(OnQueryChanged(it)) },
                        onClear = { onEvent(OnQueryChanged("")) },
                        placeholder = localization.searchPlaceholder
                    )

                    if (state.products.isEmpty()) {
                        ProductsEmptyState(
                            title = if (state.searchQuery.isNullOrBlank())
                                localization.noProductsTitle
                            else
                                localization.noResultsTitle,
                            message = if (state.searchQuery.isNullOrBlank())
                                localization.emptyMessage
                            else
                                localization.noResultsMessage(state.searchQuery),
                        )
                    } else {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.products,
                                key = { product -> product.id }
                            ) { item ->
                                ProductCardItem(
                                    product = item,
                                    onItemClick = {
                                        onEvent(OnProductClicked(item.id))
                                    },
                                    onFavoriteClick = {
                                        onEvent(OnFavoriteClicked(item.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatePlaceholder(
    icon: ImageVector,
    iconTint: Color,
    iconBackground: Color,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(44.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 320.dp)
            )

            if (action != null) {
                action()
            }
        }
    }
}

@Composable
fun ProductsErrorState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null
) {
    val localization = LocalFeatureLocalization.current
    StatePlaceholder(
        icon = Icons.Outlined.CloudOff,
        iconTint = MaterialTheme.colorScheme.error,
        iconBackground = MaterialTheme.colorScheme.errorContainer,
        title = title ?: localization.errorTitle(null),
        message = message ?: localization.errorMessage(null),
        modifier = modifier,
        action = {
            Button(onClick = onRetry) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = localization.retry,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    )
}

@Composable
fun ProductsEmptyState(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    StatePlaceholder(
        icon = Icons.Outlined.Inventory2,
        iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
        iconBackground = MaterialTheme.colorScheme.surfaceVariant,
        title = title,
        message = message,
        modifier = modifier,
        action = if (actionLabel != null && onAction != null) {
            {
                OutlinedButton(onClick = onAction) {
                    Text(actionLabel)
                }
            }
        } else null
    )
}

@Composable
fun ProductCardItem(
    modifier: Modifier = Modifier,
    product: ProductItemUiModel,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val localization = LocalFeatureLocalization.current
    Card(
        onClick = { onItemClick() },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = product.thumbnail,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )

                if (product.discountPercentage > 0.0) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "-${product.discountPercentage.roundToInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (product.isFavorite)
                            Icons.Filled.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = if (product.isFavorite)
                            localization.removeFromFavorites
                        else
                            localization.addToFavorites,
                        tint = if (product.isFavorite)
                            Color(0xFFE53935)
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    onFavoriteClick()
                                }
                            )
                    )
                }
            }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (product.brand.isNotBlank()) {
                    Text(
                        text = product.brand,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = String.format(Locale.US, "%.1f", product.rating),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                PriceRow(
                    price = product.price,
                    discountPercentage = product.discountPercentage
                )
            }
        }
    }
}

@Composable
private fun PriceRow(price: Double, discountPercentage: Double) {
    val hasDiscount = discountPercentage > 0.0
    val finalPrice = price * (1 - discountPercentage / 100)

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "$${String.format(Locale.US, "%.2f", finalPrice)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        if (hasDiscount) {
            Text(
                text = "$${String.format(Locale.US, "%.2f", price)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}

@Composable
fun ProductSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search products",
    onClear: () -> Unit = {}
) {
    val localization = LocalFeatureLocalization.current
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onClear()
                        keyboardController?.hide()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = localization.clearSearch,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FoundationTaskTheme {
        ProductsListScreen(
            state = ProductListUiState.Success(
                products = (1..10).map { index ->
                    ProductItemUiModel(
                        brand = "Brand $index",
                        category = "Category $index",
                        description = "Description for product $index",
                        discountPercentage = (5..30).random().toDouble(),
                        id = index,
                        images = listOf("https://picsum.photos/300/300?random=$index"),
                        price = (100..1000).random().toDouble(),
                        rating = (30..50).random() / 10.0,
                        stock = (1..100).random(),
                        thumbnail = "https://picsum.photos/100/100?random=$index",
                        title = "Product $index",
                        isFavorite = false
                    )
                }
            ),
            onEvent = {}
        )
    }
}