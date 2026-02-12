package com.viewnext.domain.usecase

import com.viewnext.domain.model.Detalles
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
     */
    suspend operator fun invoke(): Result<List<Detalles>> =
        repository.refreshDetalles()
}