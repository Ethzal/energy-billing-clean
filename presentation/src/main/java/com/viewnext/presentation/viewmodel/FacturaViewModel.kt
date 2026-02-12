package com.viewnext.presentation.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viewnext.domain.model.Factura
import com.viewnext.domain.usecase.FilterFacturasUseCase
import com.viewnext.domain.usecase.GetFacturasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel encargado de manejar la lógica de presentación de facturas,
 * incluyendo la carga, filtrado y estado de la UI (loading/error)
 */
@HiltViewModel
class FacturaViewModel @Inject constructor(
    private val getFacturasUseCase: GetFacturasUseCase,
    private val filterFacturasUseCase: FilterFacturasUseCase
) : ViewModel() {

    data class FacturaUiState(
        val facturas: List<Factura> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val fechaInicio: String? = null,
        val fechaFin: String? = null,
        val valoresSlider: List<Float>? = null,
        val estados: List<String>? = null,
        val mensaje: String? = null
    )

    private val _uiState = MutableStateFlow(FacturaUiState())
    val uiState: StateFlow<FacturaUiState> = _uiState.asStateFlow()

    private var facturasOriginales: MutableList<Factura> = mutableListOf()

    /**
     * Carga las facturas desde el UseCase.
     * Si hay filtros activos, los aplica automáticamente.
     * Actualiza loading y errorMessage según el resultado.
     * @param usingRetromock si se debe usar la fuente de datos de prueba
     */
    fun loadFacturas(usingRetromock: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getFacturasUseCase(usingRetromock).fold(
                onSuccess = { facturas ->
                    facturasOriginales = facturas.toMutableList()

                    if (hayFiltrosActivos()) {
                        aplicarFiltrosInterno()
                    } else {
                        if (facturasOriginales.isEmpty()) {
                            _uiState.update { it.copy(facturas = emptyList(), mensaje = "No hay facturas") }
                        } else {
                            _uiState.update { it.copy(facturas = facturasOriginales, mensaje = null) }
                        }
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun clearMensaje() {
        _uiState.update { it.copy(mensaje = null) }
    }

    fun setFechaInicio(fecha: String?) {
        _uiState.update { it.copy(fechaInicio = fecha) }
        aplicarFiltrosSiHayDatos()
    }

    fun setFechaFin(fecha: String?) {
        _uiState.update { it.copy(fechaFin = fecha) }
        aplicarFiltrosSiHayDatos()
    }

    fun setValoresSlider(valores: List<Float>?) {
        _uiState.update { it.copy(valoresSlider = valores) }
        aplicarFiltrosSiHayDatos()
    }

    fun setEstados(estados: List<String>?) {
        _uiState.update { it.copy(estados = estados) }
        aplicarFiltrosSiHayDatos()
    }

    /**
     * Obtiene el importe máximo de las facturas originales.
     * @return importe máximo o 0 si no hay facturas
     */
    fun getMaxImporte(): Float {
        if (facturasOriginales.isEmpty()) return 0f
        return facturasOriginales.maxOfOrNull { it.importeOrdenacion.toFloat() } ?: 0f
    }

    /**
     * Comprueba si algún filtro (estado, fechas, importe) está activo.
     * @return true si hay algún filtro activo, false si no
     */
    fun hayFiltrosActivos(): Boolean {
        val state = _uiState.value
        val estadosValue = state.estados
        val fechaInicioValue = state.fechaInicio
        val fechaFinValue = state.fechaFin
        val sliderValue = state.valoresSlider

        return listOfNotNull(
            estadosValue?.let { it.takeIf { list -> list.isNotEmpty() } },
            fechaInicioValue?.takeIf { it != "día/mes/año" },
            fechaFinValue.takeIf { it != "día/mes/año" },
            sliderValue?.takeIf {
                it.size == 2 && (it[0] > 0f || it[1] < getMaxImporte())
            }
        ).isNotEmpty()
    }

    // ACTIVITY
    fun init(primeraVez: Boolean, intent: Intent) {
        if (primeraVez) {
            val usingRetromock = intent.getBooleanExtra("USING_RETROMOCK", false)
            if (hayFiltrosActivos()) {
                aplicarFiltrosPorDefecto()
            } else {
                loadFacturas(usingRetromock)
            }
        }
    }

    private fun aplicarFiltrosSiHayDatos() {
        if (facturasOriginales.isNotEmpty()) {
            aplicarFiltrosInterno()
        }
    }

    private fun aplicarFiltrosInterno() {
        val state = _uiState.value
        val resultado = filterFacturasUseCase.filtrarFacturas(
            facturasOriginales,
            state.estados?.toMutableList(),
            state.fechaInicio,
            state.fechaFin,
            state.valoresSlider?.getOrNull(0)?.toDouble(),
            state.valoresSlider?.getOrNull(1)?.toDouble()
        )

        val mensaje = if (resultado.isEmpty()) {
            "No hay facturas que coincidan con los filtros"
        } else null

        _uiState.update {
            it.copy(
                facturas = resultado.toMutableList(),
                mensaje = mensaje
            )
        }
    }

    fun clearFiltros() {
        _uiState.update {
            it.copy(
                estados = null,
                fechaInicio = null,
                fechaFin = null,
                valoresSlider = null
            )
        }

        // Restaurar lista original directamente
        if (facturasOriginales.isNotEmpty()) {
            _uiState.update {
                it.copy(facturas = facturasOriginales, mensaje = null)
            }
        }
    }

    private fun aplicarFiltrosPorDefecto() {
        val state = _uiState.value

        val fechaInicioStr = state.fechaInicio
        val fechaFinStr = state.fechaFin
        val sliderValues = state.valoresSlider ?: listOf(0f, getMaxImporte())
        val estadosDefecto = state.estados?.toMutableList() ?: mutableListOf()

        val resultado = filterFacturasUseCase.filtrarFacturas(
            facturasOriginales,
            estadosDefecto,
            fechaInicioStr,
            fechaFinStr,
            sliderValues[0].toDouble(),
            sliderValues[1].toDouble()
        )

        _uiState.update { it.copy(facturas = resultado) }
    }
}
