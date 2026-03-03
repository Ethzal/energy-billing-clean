package com.viewnext.presentation.ui.smartsolar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.viewnext.presentation.composables.smartsolar.SmartSolarScreen
import com.viewnext.presentation.ui.theme.EnergyAppTheme
import com.viewnext.presentation.viewmodel.DetallesViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal de Smart Solar.
 * Muestra una interfaz con un ViewPager2 y un TabLayout para navegar
 * entre las secciones "Mi Instalación", "Energía" y "Detalles".
 * También gestiona el edge-to-edge layout y el botón de retroceso.
 */
@AndroidEntryPoint
class SmartSolarActivity : ComponentActivity() {
    private val detallesViewModel: DetallesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EnergyAppTheme {
                SmartSolarScreen(
                    detallesViewModel = detallesViewModel,
                    onBack = { onBackPressedDispatcher.onBackPressed() }
                )
            }
        }
    }
}
