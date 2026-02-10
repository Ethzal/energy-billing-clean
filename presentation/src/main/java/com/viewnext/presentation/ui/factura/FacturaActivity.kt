package com.viewnext.presentation.ui.factura

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.viewnext.domain.model.Factura
import com.viewnext.presentation.R
import com.viewnext.presentation.adapter.FacturaAdapter
import com.viewnext.presentation.databinding.ActivityFacturaBinding
import com.viewnext.presentation.viewmodel.FacturaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.Int
import kotlin.String

/**
 * Activity principal para la pantalla de facturas.
 * Funcionalidades:
 * - Mostrar lista de facturas en RecyclerView.
 * - Aplicar filtros mediante FiltroFragment.
 * - Skeleton (Shimmer) mientras se cargan datos.
 * - Manejo de errores y mensajes de estado.
 */
@AndroidEntryPoint
class FacturaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacturaBinding
    private lateinit var facturaViewModel: FacturaViewModel
    private lateinit var adapter: FacturaAdapter
    private lateinit var facturaNavigator: FacturaNavigator

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        binding = ActivityFacturaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
        // Navigator
        facturaNavigator = FacturaNavigator(supportFragmentManager)

        // Hacer visible el fragmento tras rotar
        val filtroFragment =
            supportFragmentManager.findFragmentByTag("FILTRO_FRAGMENT") as FiltroFragment?
        if (filtroFragment != null) {
            binding.fragmentContainer.visibility = View.VISIBLE
        }

        // Creacion ViewModel de Factura
        facturaViewModel =
            ViewModelProvider(this)[FacturaViewModel::class.java]

        // Adapter
        adapter = FacturaAdapter(mutableListOf())
        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
        binding.recyclerView.setAdapter(adapter)

        // Pop-Up
        adapter.setOnFacturaClickListener { _: Factura? ->
            AlertDialog.Builder(this@FacturaActivity)
                .setTitle(R.string.info)
                .setMessage(R.string.funcionalidad_no_disponible)
                .setPositiveButton(
                    R.string.cerrar
                ) { dialog: DialogInterface?, _: Int -> dialog?.dismiss() }
                .show()
        }

        // Mostrar skeleton
        showShimmer()

        // Primera vez
        facturaViewModel.init(savedInstanceState == null, intent)

        facturaViewModel.loading.observe(this) { isLoading ->
            if (isLoading == true) showShimmer() else hideShimmer()
        }

        // Toolbar
        setSupportActionBar(binding.toolbar)

        // Botón atrás
        binding.backButton.setOnClickListener { _: View? -> onBackPressedDispatcher.onBackPressed() }

        // Configuración del título
        binding.toolbarTitle.text = "Facturas"

        // Actualizar RecyclerView con las nuevas facturas
        facturaViewModel.facturas.observe(this) { facturas ->
            facturas?.filterNotNull()?.let { listaNoNula ->
                if (listaNoNula.isEmpty() && facturaViewModel.hayFiltrosActivos()) {
                    Toast.makeText(this, "No hay facturas", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.setFacturas(listaNoNula.toMutableList())
                }
            }
        }

        // Mostrar mensaje de error
        facturaViewModel.errorMessage.observe(this, Observer { error: String? ->
            if (error != null) {
                Toast.makeText(this@FacturaActivity, error, Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Mostrar/ocultar skeleton
    private fun showShimmer() {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideShimmer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Crear menú para el botón filtros
        menuInflater.inflate(R.menu.menu_factura, menu)
        return true
    }

    /*******************
     * NAVIGATOR
     *******************/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filters) {
            val maxImporte = facturaViewModel.getMaxImporte()
            facturaNavigator.openFilter(maxImporte)
            binding.fragmentContainer.visibility = View.VISIBLE
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() { // Sobreescribir el metodo deprecated por incompatibilidades
        if (!facturaNavigator.handleBackPressed()) {
            super.onBackPressed()
        }
    }

    fun restoreMainView() { // Restaurar visibilidad de la actividad Factura
        binding.toolbar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.fragmentContainer.visibility = View.GONE
    }

    fun aplicarFiltros(bundle: Bundle) {
        // Recuperar los filtros desde el Bundle
        val estadosSeleccionados: MutableList<String?>? = bundle.getStringArrayList("ESTADOS")
        val fechaInicio = bundle.getString("FECHA_INICIO")
        val fechaFin = bundle.getString("FECHA_FIN")
        val importeMin = bundle.getDouble("IMPORTE_MIN")
        val importeMax = bundle.getDouble("IMPORTE_MAX")

        // Llamar al ViewModel para aplicar los filtros
        facturaViewModel.aplicarFiltros(
            estadosSeleccionados,
            fechaInicio,
            fechaFin,
            importeMin,
            importeMax
        )
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}