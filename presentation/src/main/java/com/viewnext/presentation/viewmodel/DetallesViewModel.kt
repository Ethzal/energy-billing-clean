package com.viewnext.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.viewnext.domain.model.Detalles
import com.viewnext.domain.repository.DetallesCallback
import com.viewnext.domain.repository.GetDetallesRepository
import com.viewnext.domain.usecase.GetDetallesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel encargado de manejar la lógica de presentación de los detalles.
 * Gestiona la carga de datos desde el UseCase y expone los resultados mediante LiveData.
 */
@HiltViewModel
class DetallesViewModel
/**
 * Constructor con inyección de dependencias de UseCase y Repository.
 * @param useCase caso de uso para obtener detalles
 * @param repository repositorio para acceso a datos
 */ @Inject constructor(
    private val useCase: GetDetallesUseCase,
    private val repository: GetDetallesRepository?
) : ViewModel() {
    /**
     * Devuelve LiveData con la lista de detalles.
     * @return LiveData observables de detalles
     */
    val detalles: MutableLiveData<List<Detalles>> = MutableLiveData(emptyList())

    /**
     * Carga los detalles usando el UseCase y publica los resultados en detallesLiveData.
     * En caso de error, actualmente no se maneja pero se podría exponer un LiveData de error.
     */
    fun loadDetalles() {
        useCase.refreshDetalles(object : DetallesCallback<List<Detalles>> {
            override fun onSuccess(result: List<Detalles>) {
                detalles.postValue(result) // Actualiza la lista de detalles
            }

            override fun onFailure(error: Throwable) {
                Log.e("DetallesViewModel", "Error al cargar detalles", error)
            }
        })
    }
}