package com.example.foundationtask.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.foundationtask.ui.details.ProductDetailsUiEvent.*
import com.example.foundationtask.ui.list.ProductsErrorState
import com.example.foundationtask.ui.localization.LocalFeatureLocalization
import com.example.foundationtask.ui.model.ProductItemUiModel
import com.example.foundationtask.ui.theme.FoundationTaskTheme
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    state: ProductDetailsUiState,
    onEvent: (ProductDetailsUiEvent) -> Unit
) {
    val localization = LocalFeatureLocalization.current
    when (state) {
        is ProductDetailsUiState.Success -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = state.product.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    onEvent(OnClickBackIcon)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = localization.back
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    onEvent(
                                        OnFavoriteClicked(productId = state.product.id)
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = if (state.product.isFavorite)
                                        Icons.Filled.Favorite
                                    else
                                        Icons.Outlined.FavoriteBorder,
                                    contentDescription = if (state.product.isFavorite)
                                        localization.removeFromFavorites
                                    else
                                        localization.addToFavorites,
                                    tint = if (state.product.isFavorite)
                                        Color(0xFFE53935)
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    ProductImagePager(
                        images = state.product.images.ifEmpty {
                            listOf(state.product.thumbnail)
                        }
                    )

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProductHeader(state.product)
                        PriceSection(
                            price = state.product.price,
                            discountPercentage = state.product.discountPercentage
                        )
                        StockBadge(stock = state.product.stock)
                        DescriptionSection(description = state.product.description)
                    }
                }
            }
        }

        is ProductDetailsUiState.Failure -> {
            ProductsErrorState(
                onRetry = { onEvent(LoadProductById(state.productId)) },
                title = localization.errorTitle(state.exception),
                message = localization.errorMessage(state.exception)
            )
        }

        is ProductDetailsUiState.Loading -> {
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
    }
}

@Composable
private fun ProductImagePager(images: List<String>) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Box(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(images.size) { index ->
                    PageIndicatorDot(pagerState = pagerState, index = index)
                }
            }
        }
    }
}

@Composable
private fun PageIndicatorDot(pagerState: PagerState, index: Int) {
    val selected by remember(index) {
        derivedStateOf { pagerState.currentPage == index }
    }
    Box(
        modifier = Modifier
            .size(if (selected) 9.dp else 7.dp)
            .clip(CircleShape)
            .background(
                if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
    )
}

@Composable
private fun ProductHeader(product: ProductItemUiModel) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            RatingChip(rating = product.rating)
        }

        if (product.brand.isNotBlank()) {
            Text(
                text = product.brand,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AssistChip(
            onClick = {},
            label = { Text(product.category) },
            colors = AssistChipDefaults.assistChipColors()
        )
    }
}

@Composable
private fun RatingChip(rating: Double) {
    val localization = LocalFeatureLocalization.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = localization.ratingLabel,
            tint = Color(0xFFFFC107),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = String.format(Locale.US, "%.1f", rating),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PriceSection(price: Double, discountPercentage: Double) {
    val hasDiscount = discountPercentage > 0.0
    val finalPrice = price * (1 - discountPercentage / 100)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "$${String.format(Locale.US, "%.2f", finalPrice)}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        if (hasDiscount) {
            Text(
                text = "$${String.format(Locale.US, "%.2f", price)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.LineThrough
            )
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.errorContainer
            ) {
                Text(
                    text = "-${discountPercentage.roundToInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun StockBadge(stock: Int) {
    val localization = LocalFeatureLocalization.current
    val (label, color) = when {
        stock <= 0 -> localization.outOfStock to MaterialTheme.colorScheme.error
        stock < 10 -> localization.onlyLeft(stock) to Color(0xFFEF6C00)
        else -> localization.inStock(stock) to Color(0xFF2E7D32)
    }
    Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium,
        color = color
    )
}

@Composable
private fun DescriptionSection(description: String) {
    val localization = LocalFeatureLocalization.current
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = localization.descriptionLabel,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(1.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FoundationTaskTheme {
        ProductDetailsScreen(
            state = ProductDetailsUiState.Success(
                ProductItemUiModel(
                    brand = "Apple",
                    category = "Smartphones",
                    description = "Latest iPhone with advanced camera system.",
                    discountPercentage = 10.0,
                    id = 1,
                    images = listOf(
                        "https://example.com/iphone1.jpg",
                        "https://example.com/iphone2.jpg"
                    ),
                    price = 999.99,
                    rating = 4.8,
                    stock = 50,
                    thumbnail = "https://example.com/iphone-thumb.jpg",
                    title = "iPhone 16 Pro",
                    isFavorite = true
                )
            ),
            onEvent = {}
        )
    }
}