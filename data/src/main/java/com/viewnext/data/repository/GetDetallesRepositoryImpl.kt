package com.viewnext.data.repository

import android.content.Context
import android.util.Log
import com.viewnext.data.api.ApiService
import com.viewnext.data.api.RetromockClient
import com.viewnext.domain.model.Detalles
import com.viewnext.domain.repository.GetDetallesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.Result
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio GetDetallesRepository. Esta clase se encarga de obtener los detalles
 * de una fuente remota (simulada por Retromock) y almacenarlos en una lista en caché.
 */
@Singleton
class GetDetallesRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : GetDetallesRepository {

    private val apiServiceMock: ApiService =
        RetromockClient.getRetromockInstance(context).create(ApiService::class.java)

    private val detallesCache = mutableListOf<Detalles>()

    // Flow que expone siempre el último valor
    private val _detallesFlow = MutableStateFlow<List<Detalles>>(emptyList())

    override fun getDetallesFlow(): Flow<List<Detalles>> = _detallesFlow.asStateFlow()

    /**
     * Realiza una solicitud a la API para obtener la lista de [Detalles].
     * Si la respuesta es exitosa, actualiza la caché interna y emite la nueva lista
     * a través del flujo correspondiente.
     * @return [Result.success] con la lista de detalles si la operación fue exitosa,
     * o [Result.failure] en caso de error durante la solicitud o el procesamiento.
     */

    override suspend fun refreshDetalles(): Result<List<Detalles>> =
        withContext(Dispatchers.IO) {
            apiServiceMock.detallesMock
                ?.awaitResponse()
                ?.takeIf { it.isSuccessful }
                ?.body()
                ?.let { body ->
                    detallesCache.clear()
                    detallesCache.addAll(body.detalles)
                    _detallesFlow.value = detallesCache.toList()
                    Log.d("Repository", "Detalles cargados: ${detallesCache.size}")
                    Result.success(detallesCache.toList())
                } ?: Result.failure(Exception("Error al cargar detalles"))
        }
}