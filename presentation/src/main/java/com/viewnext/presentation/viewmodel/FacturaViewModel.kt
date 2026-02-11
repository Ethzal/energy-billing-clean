package com.viewnext.presentation.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.viewnext.domain.model.Factura
import com.viewnext.domain.usecase.FilterFacturasUseCase
import com.viewnext.domain.usecase.GetFacturasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _facturasLiveData = MutableLiveData<List<Factura>>()
    val facturas: LiveData<List<Factura>> get() = _facturasLiveData

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?> get() = _loading

    val fechaInicio = MutableLiveData<String?>()
    val fechaFin = MutableLiveData<String?>()
    val valoresSlider = MutableLiveData<List<Float>?>()
    val estados = MutableLiveData<List<String>?>()

    private var facturasOriginales: MutableList<Factura> = ArrayList()
    private var facturasFiltradas: MutableList<Factura> = ArrayList()

    /**
     * Carga las facturas desde el UseCase.
     * Si hay filtros activos, los aplica automáticamente.
     * Actualiza loading y errorMessage según el resultado.
     * @param usingRetromock si se debe usar la fuente de datos de prueba
     */
    fun loadFacturas(usingRetromock: Boolean) {
        _loading.postValue(true)

        getFacturasUseCase.execute(usingRetromock, object : GetFacturasUseCase.Callback {
            override fun onSuccess(facturas: MutableList<Factura>) {
                facturasOriginales = ArrayList(facturas.toList())

                if (hayFiltrosActivos()) {
                    aplicarFiltros(
                        estados.value?.toMutableList(),
                        fechaInicio.value,
                        fechaFin.value,
                        valoresSlider.value?.getOrNull(0)?.toDouble(),
                        valoresSlider.value?.getOrNull(1)?.toDouble()
                    )
                } else {
                    _facturasLiveData.postValue(facturasOriginales)
                }

                _loading.postValue(false)
            }

            override fun onError(error: String?) {
                _errorMessage.postValue(error)
                _loading.postValue(false)
            }
        })
    }

    /**
     * Aplica los filtros seleccionados sobre las facturas originales.
     * Publica el resultado en facturasLiveData.
     * @param estadosSeleccionados lista de estados a filtrar
     * @param fechaInicio fecha mínima de la factura
     * @param fechaFin fecha máxima de la factura
     * @param importeMin importe mínimo
     * @param importeMax importe máximo
     */
    fun aplicarFiltros(
        estadosSeleccionados: MutableList<String?>?,
        fechaInicio: String?,
        fechaFin: String?,
        importeMin: Double?,
        importeMax: Double?
    ) {
        if (facturasOriginales.isEmpty()) return

        val resultado = filterFacturasUseCase.filtrarFacturas(
            facturasOriginales,
            estadosSeleccionados,
            fechaInicio,
            fechaFin,
            importeMin,
            importeMax
        ).filterNotNull()

        facturasFiltradas = ArrayList(resultado)
        _facturasLiveData.postValue(facturasFiltradas)
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
        val estadosValue = estados.value
        val fechaInicioValue = fechaInicio.value
        val fechaFinValue = fechaFin.value
        val sliderValue = valoresSlider.value

        return (estadosValue != null && estadosValue.isNotEmpty()) ||
                (fechaInicioValue != null && fechaInicioValue != "día/mes/año") ||
                (fechaFinValue != null && fechaFinValue != "día/mes/año") ||
                (sliderValue != null && sliderValue.size == 2 &&
                        (sliderValue[0] > 0f || sliderValue[1] < getMaxImporte()))
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

    private fun aplicarFiltrosPorDefecto() {
        val fechaInicioStr = fechaInicio.value ?: ""
        val fechaFinStr = fechaFin.value ?: ""
        val sliderValues = valoresSlider.value

        val valoresDefecto = if (sliderValues == null || sliderValues.size < 2) {
            listOf(0f, getMaxImporte())
        } else sliderValues

        aplicarFiltros(
            (estados.value ?: emptyList()).toMutableList(),
            fechaInicioStr,
            fechaFinStr,
            valoresDefecto[0].toDouble(),
            valoresDefecto[1].toDouble()
        )
    }
}
