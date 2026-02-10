package com.viewnext.domain.usecase

import com.viewnext.domain.model.Detalles
import com.viewnext.domain.repository.DetallesCallback
import com.viewnext.domain.repository.GetDetallesRepository

/**
 * Caso de uso para obtener los detalles de la instalación desde el repositorio.
 * Encapsula la lógica de negocio para la recuperación de detalles.
 */
class GetDetallesUseCase
/**
 * Constructor del caso de uso.
 * @param repository Repositorio que maneja la obtención de detalles
 */(private val repository: GetDetallesRepository) {
    /**
     * Ejecuta la actualización de los detalles.
     * @param callback Callback que será notificado con los resultados
     */
    fun refreshDetalles(callback: DetallesCallback<List<Detalles>>) {
        repository.refreshDetalles(object : DetallesCallback<List<Detalles>> {
            override fun onSuccess(result: List<Detalles>) {
                callback.onSuccess(result)
            }

            override fun onFailure(error: Throwable) {
                callback.onFailure(error)
            }
        })
    }
}