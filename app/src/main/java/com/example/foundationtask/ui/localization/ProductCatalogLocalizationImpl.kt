package com.example.foundationtask.ui.localization

import android.content.Context
import com.example.foundationtask.R
import com.example.foundationtask.domain.exception.DomainException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductCatalogLocalizationImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ProductCatalogLocalization {

    override val searchPlaceholder: String
        get() = context.getString(R.string.product_catalog_search_placeholder)

    override val clearSearch: String
        get() = context.getString(R.string.product_catalog_clear_search)

    override val noProductsTitle: String
        get() = context.getString(R.string.product_catalog_no_products_title)

    override val noResultsTitle: String
        get() = context.getString(R.string.product_catalog_no_results_title)

    override val emptyMessage: String
        get() = context.getString(R.string.product_catalog_empty_message)

    override val retry: String
        get() = context.getString(R.string.product_catalog_retry)

    override val addToFavorites: String
        get() = context.getString(R.string.product_catalog_add_to_favorites)

    override val removeFromFavorites: String
        get() = context.getString(R.string.product_catalog_remove_from_favorites)

    override val back: String
        get() = context.getString(R.string.product_catalog_back)

    override val ratingLabel: String
        get() = context.getString(R.string.product_catalog_rating)

    override val outOfStock: String
        get() = context.getString(R.string.product_catalog_out_of_stock)

    override val descriptionLabel: String
        get() = context.getString(R.string.product_catalog_description)

    override fun errorTitle(exception: DomainException?): String = context.getString(
        when (exception) {
            is DomainException.NetworkUnavailableException -> R.string.product_catalog_error_network_title
            is DomainException.ServerErrorException -> R.string.product_catalog_error_server_title
            is DomainException.ClientErrorException -> R.string.product_catalog_error_client_title
            is DomainException.AuthenticationException -> R.string.product_catalog_error_auth_title
            is DomainException.DataParsingException -> R.string.product_catalog_error_parsing_title
            else -> R.string.product_catalog_error_title
        }
    )

    override fun errorMessage(exception: DomainException?): String = context.getString(
        when (exception) {
            is DomainException.NetworkUnavailableException -> R.string.product_catalog_error_network_message
            is DomainException.ServerErrorException -> R.string.product_catalog_error_server_message
            is DomainException.ClientErrorException -> R.string.product_catalog_error_client_message
            is DomainException.AuthenticationException -> R.string.product_catalog_error_auth_message
            is DomainException.DataParsingException -> R.string.product_catalog_error_parsing_message
            else -> R.string.product_catalog_error_message
        }
    )

    override fun priceLabel(amount: String): String =
        context.getString(R.string.product_catalog_price_label, amount)

    override fun itemCount(count: Int): String =
        context.resources.getQuantityString(
            R.plurals.product_catalog_item_count,
            count,
            count
        )

    override fun noResultsMessage(query: String): String =
        context.getString(R.string.product_catalog_no_results_message, query)

    override fun onlyLeft(count: Int): String =
        context.getString(R.string.product_catalog_only_left, count)

    override fun inStock(count: Int): String =
        context.getString(R.string.product_catalog_in_stock, count)
}
