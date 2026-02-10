package com.viewnext.presentation.ui.smartsolar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.viewnext.presentation.R
import com.viewnext.presentation.adapter.SmartSolarPagerAdapter
import com.viewnext.presentation.databinding.ActivitySmartSolarBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal de Smart Solar.
 * Muestra una interfaz con un ViewPager2 y un TabLayout para navegar
 * entre las secciones "Mi Instalación", "Energía" y "Detalles".
 * También gestiona el edge-to-edge layout y el botón de retroceso.
 */
@AndroidEntryPoint
class SmartSolarActivity : AppCompatActivity() {
    private var binding: ActivitySmartSolarBinding? = null
    private val tabLayout: TabLayout? = null
    private val viewPager2: ViewPager2? = null
    private val adapter: SmartSolarPagerAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.enableEdgeToEdge()
        binding = ActivitySmartSolarBinding.inflate(layoutInflater)
        setContentView(binding?.getRoot())
        binding?.root?.let { rootView ->
            ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // Botón atrás
        binding?.backButton?.setOnClickListener { _: View? -> onBackPressedDispatcher.onBackPressed() }

        // Configuración del título
        binding?.toolbarTitle?.text = "Smart Solar"

        // Creación de lista de fragmentos
        val fragments = mutableListOf(
            MiInstalacionFragment(),
            EnergiaFragment(),
            DetallesFragment()
        )

        // Configurar ViewPager2 con el adaptador
        val adapter =
            SmartSolarPagerAdapter(this, fragments)

        binding?.viewPager?.setAdapter(adapter)

        // Vincular TabLayout con ViewPager2
        binding?.tabLayout?.let {
            binding?.viewPager?.let { viewPager ->
                TabLayoutMediator(
                    it,
                    viewPager
                ) { tab: TabLayout.Tab?, position: Int ->
                    when (position) {
                        0 -> tab?.setText("Mi Instalación")
                        1 -> tab?.setText("Energía")
                        2 -> tab?.setText("Detalles")
                    }
                }
            }
        }?.attach()

        // Aplicar el fondo personalizado al TabLayout
        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.view.setBackgroundResource(R.drawable.tab_selector)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.view.setBackgroundResource(android.R.color.white)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}