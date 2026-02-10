package com.viewnext.presentation.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.viewnext.domain.repository.DetallesCallback;
import com.viewnext.domain.repository.GetDetallesRepository;
import com.viewnext.domain.usecase.GetDetallesUseCase;
import com.viewnext.domain.model.Detalles;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel encargado de manejar la lógica de presentación de los detalles.
 * Gestiona la carga de datos desde el UseCase y expone los resultados mediante LiveData.
 */
@HiltViewModel
public class DetallesViewModel extends ViewModel {
    private final GetDetallesUseCase useCase;
    private final MutableLiveData<List<Detalles>> detallesLiveData = new MutableLiveData<>();
    private final GetDetallesRepository repository;

    /**
     * Constructor con inyección de dependencias de UseCase y Repository.
     * @param useCase caso de uso para obtener detalles
     * @param repository repositorio para acceso a datos
     */
    @Inject
    public DetallesViewModel(GetDetallesUseCase useCase, GetDetallesRepository repository) {
        this.useCase = useCase;
        this.repository = repository;
    }

    /**
     * Devuelve LiveData con la lista de detalles.
     * @return LiveData observables de detalles
     */
    public MutableLiveData<List<Detalles>> getDetalles() {
        return detallesLiveData;
    }

    /**
     * Carga los detalles usando el UseCase y publica los resultados en detallesLiveData.
     * En caso de error, actualmente no se maneja pero se podría exponer un LiveData de error.
     */
    public void loadDetalles() {
        useCase.refreshDetalles(new DetallesCallback<>() {
            @Override
            public void onSuccess(List<Detalles> result) {
                detallesLiveData.setValue(result);  // Actualiza los detalles
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e("DetallesViewModel", "Error al cargar detalles", error);
            }
        });
    }
}