package com.viewnext.presentation.viewmodel;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.viewnext.domain.model.Factura;
import com.viewnext.domain.usecase.FilterFacturasUseCase;
import com.viewnext.domain.usecase.GetFacturasUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel encargado de manejar la lógica de presentación de facturas,
 * incluyendo la carga, filtrado y estado de la UI (loading/error)
 */
@HiltViewModel
public class FacturaViewModel extends ViewModel {

    private final MutableLiveData<List<Factura>> facturasLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final GetFacturasUseCase getFacturasUseCase;
    private final FilterFacturasUseCase filterFacturasUseCase;

    private List<Factura> facturasOriginales = new ArrayList<>();
    private List<Factura> facturasFiltradas = new ArrayList<>();

    // LiveData que mantiene los filtros activos para la UI
    private final MutableLiveData<String> fechaInicio = new MutableLiveData<>();
    private final MutableLiveData<String> fechaFin = new MutableLiveData<>();
    private final MutableLiveData<List<Float>> valoresSlider = new MutableLiveData<>();
    private final MutableLiveData<List<String>> estados = new MutableLiveData<>();

    /**
     * Constructor con inyección de dependencias de los UseCases.
     * @param getFacturasUseCase UseCase para obtener facturas
     * @param filterFacturasUseCase UseCase para filtrar facturas
     */
    @Inject
    public FacturaViewModel(
            GetFacturasUseCase getFacturasUseCase,
            FilterFacturasUseCase filterFacturasUseCase
    ) {
        this.getFacturasUseCase = getFacturasUseCase;
        this.filterFacturasUseCase = filterFacturasUseCase;
    }

    // Getters LiveData
    public LiveData<List<Factura>> getFacturas() {
        return facturasLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<String> getFechaInicio() {
        return fechaInicio;
    }

    public MutableLiveData<String> getFechaFin() {
        return fechaFin;
    }

    public MutableLiveData<List<Float>> getValoresSlider() {
        return valoresSlider;
    }

    public MutableLiveData<List<String>> getEstados() {
        return estados;
    }

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    /**
     * Carga las facturas desde el UseCase.
     * Si hay filtros activos, los aplica automáticamente.
     * Actualiza loading y errorMessage según el resultado.
     * @param usingRetromock si se debe usar la fuente de datos de prueba
     */
    public void loadFacturas(boolean usingRetromock) {
        loading.postValue(true);

        getFacturasUseCase.execute(usingRetromock, new GetFacturasUseCase.Callback() {
            @Override
            public void onSuccess(List<Factura> facturas) {
                facturasOriginales = new ArrayList<>(facturas);

                if (hayFiltrosActivos()) {
                    aplicarFiltros(
                            estados.getValue(),
                            fechaInicio.getValue(),
                            fechaFin.getValue(),
                            valoresSlider != null && valoresSlider.getValue() != null && valoresSlider.getValue().size() == 2
                                    ? (double) valoresSlider.getValue().get(0)
                                    : null,
                            valoresSlider != null && valoresSlider.getValue() != null && valoresSlider.getValue().size() == 2
                                    ? (double) valoresSlider.getValue().get(1)
                                    : null
                    );
                } else {
                    facturasLiveData.postValue(facturasOriginales);
                }

                loading.postValue(false);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
                loading.postValue(false);
            }
        });
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
    public void aplicarFiltros(List<String> estadosSeleccionados, String fechaInicio,
                               String fechaFin, Double importeMin, Double importeMax) {
        if (facturasOriginales.isEmpty()) return;

        List<Factura> facturasFiltradasResult = filterFacturasUseCase.filtrarFacturas(
                facturasOriginales,
                estadosSeleccionados,
                fechaInicio,
                fechaFin,
                importeMin,
                importeMax
        );

        facturasFiltradas = new ArrayList<>(facturasFiltradasResult);
        facturasLiveData.postValue(facturasFiltradas);
    }

    /**
     * Obtiene el importe máximo de las facturas originales.
     * @return importe máximo o 0 si no hay facturas
     */
    public float getMaxImporte() {
        if (facturasOriginales == null || facturasOriginales.isEmpty()) return 0f;

        float maxImporte = 0f;
        for (Factura factura : facturasOriginales) {
            if (factura.getImporteOrdenacion() > maxImporte) {
                maxImporte = (float) factura.getImporteOrdenacion();
            }
        }
        return maxImporte;
    }

    /**
     * Comprueba si algún filtro (estado, fechas, importe) está activo.
     * @return true si hay algún filtro activo, false si no
     */
    public boolean hayFiltrosActivos() {
        return (estados.getValue() != null && !estados.getValue().isEmpty()) ||
                (fechaInicio.getValue() != null && !fechaInicio.getValue().equals("día/mes/año")) ||
                (fechaFin.getValue() != null && !fechaFin.getValue().equals("día/mes/año")) ||
                (valoresSlider.getValue() != null &&
                        (valoresSlider.getValue().get(0) > 0 || valoresSlider.getValue().get(1) < getMaxImporte()));
    }

    // ACTIVITY
    public void init(boolean primeraVez, Intent intent) {
        if (primeraVez) {
            boolean usingRetromock = intent.getBooleanExtra("USING_RETROMOCK", false);
            if (hayFiltrosActivos()) {
                aplicarFiltrosPorDefecto();
            } else {
                loadFacturas(usingRetromock);
            }
        }
    }

    private void aplicarFiltrosPorDefecto() {
        List<String> estados = getEstados().getValue() != null ? getEstados().getValue() : new ArrayList<>();
        String fechaInicio = getFechaInicio().getValue() != null ? getFechaInicio().getValue() : "";
        String fechaFin = getFechaFin().getValue() != null ? getFechaFin().getValue() : "";
        List<Float> valoresSlider = getValoresSlider().getValue();

        if (valoresSlider == null || valoresSlider.size() < 2) {
            valoresSlider = new ArrayList<>();
            valoresSlider.add(0.0f);
            valoresSlider.add(getMaxImporte());
        }

        aplicarFiltros(estados, fechaInicio, fechaFin,
                (double) valoresSlider.get(0), (double) valoresSlider.get(1));
    }
}