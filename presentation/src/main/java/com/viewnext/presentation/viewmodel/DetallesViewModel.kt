package com.viewnext.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viewnext.domain.model.Detalles
import com.viewnext.domain.repository.GetDetallesRepository
import com.viewnext.domain.usecase.GetDetallesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    data class DetallesUiState(
        val detalles: List<Detalles> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(DetallesUiState())
    val uiState: StateFlow<DetallesUiState> = _uiState.asStateFlow()

    /**
     * Ejecuta el caso de uso para obtener los detalles y actualiza el estado de la UI.
     * Si la operación es exitosa, actualiza la lista de detalles y desactiva el estado de carga.
     * En caso de error, actualiza el mensaje de error y desactiva el estado de carga.
     */

    fun loadDetalles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            useCase().fold(
                onSuccess = { detalles ->
                    _uiState.value = _uiState.value.copy(
                        detalles = detalles,
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }
}