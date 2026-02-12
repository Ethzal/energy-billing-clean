package com.viewnext.domain.repository

import com.viewnext.domain.model.Detalles
import kotlinx.coroutines.flow.Flow

interface GetDetallesRepository {

    /**
     * Stream reactivo para la UI.
     */
    fun getDetallesFlow(): Flow<List<Detalles>>

    /**
     * Refresca los detalles desde la fuente de datos.
     */
    suspend fun refreshDetalles(): Result<List<Detalles>>
}
