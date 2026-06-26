package com.example.foundationtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.example.foundationtask.ui.localization.LocalFeatureLocalization
import com.example.foundationtask.ui.localization.ProductCatalogLocalization
import com.example.foundationtask.ui.navigation.ProductsNavHost
import com.example.foundationtask.ui.theme.FoundationTaskTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var localization: ProductCatalogLocalization

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoundationTaskTheme {
                CompositionLocalProvider(
                    LocalFeatureLocalization provides localization
                ) {
                    ProductsNavHost()
                }
            }
        }
    }
}