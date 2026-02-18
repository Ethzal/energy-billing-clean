package com.viewnext.presentation.ui.factura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import com.viewnext.presentation.composables.factura.FacturaScreen
import com.viewnext.presentation.ui.theme.EnergyAppTheme
import com.viewnext.presentation.viewmodel.FacturaViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * Activity principal para la pantalla de facturas.
 * Funcionalidades:
 * - Mostrar lista de facturas en RecyclerView.
 * - Aplicar filtros mediante FiltroFragment.
 * - Skeleton (Shimmer) mientras se cargan datos.
 * - Manejo de errores y mensajes de estado.
 */
@AndroidEntryPoint
class FacturaActivity : ComponentActivity() {
    private val viewModel: FacturaViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            EnergyAppTheme {
                FacturaScreen(
                    viewModel = viewModel,
                    onBack = { finish() },
                    usingRetromock = intent.getBooleanExtra("USING_RETROMOCK", false),
                    intent = intent
                )
            }
        }
    }
}