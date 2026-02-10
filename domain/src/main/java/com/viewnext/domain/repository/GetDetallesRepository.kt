package com.viewnext.domain.repository

import com.viewnext.domain.model.Detalles

interface GetDetallesRepository {

    /**
     * Obtiene los detalles almacenados localmente.
     */
    fun getDetalles(): List<Detalles>

    /**
     * Refresca los detalles desde la fuente de datos.
     */
    fun refreshDetalles(callback: DetallesCallback<List<Detalles>>)
}
