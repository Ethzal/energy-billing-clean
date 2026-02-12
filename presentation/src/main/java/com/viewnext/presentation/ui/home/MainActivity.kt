package com.viewnext.presentation.ui.home

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.viewnext.presentation.databinding.ActivityMainBinding
import com.viewnext.presentation.ui.factura.FacturaActivity
import com.viewnext.presentation.ui.smartsolar.SmartSolarActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal de la aplicación.
 * Funciones principales:
 * - Configuración edge-to-edge para que la UI ocupe toda la pantalla.
 * - Setup de visibilidad de controles debug.
 * - Navegación a FacturaActivity y SmartSolarActivity.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.enableEdgeToEdge() // La interfaz se extiende por toda la pantalla
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.getRoot())

        setupWindowInsets()
        setupToggleVisibility()
        setupClickListeners()
    }

    private fun setupWindowInsets() {
        binding?.root?.let { rootView ->
            ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                systemBars.let { bars ->
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
                }
                insets
            }
        }
    }

    private fun setupToggleVisibility() {
        val isDebug = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        if (isDebug) {
            binding?.btToggleApi?.visibility = View.VISIBLE
        } else {
            binding?.btToggleApi?.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        binding?.btToggleApi?.setOnClickListener { _: View? -> viewModel.toggleApi() }

        binding?.btFacturas?.setOnClickListener { _: View? ->
            val intent = Intent(this, FacturaActivity::class.java)
            intent.putExtra("USING_RETROMOCK", viewModel.usingRetromock.value)
            startActivity(intent)
        }

        binding?.btSmart?.setOnClickListener { _: View? ->
            startActivity(
                Intent(
                    this,
                    SmartSolarActivity::class.java
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
