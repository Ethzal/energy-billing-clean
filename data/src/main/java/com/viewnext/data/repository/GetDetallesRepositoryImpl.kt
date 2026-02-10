package com.viewnext.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.viewnext.data.api.ApiService
import com.viewnext.data.api.RetromockClient
import com.viewnext.domain.model.Detalles
import com.viewnext.domain.model.DetallesResponse
import com.viewnext.domain.repository.DetallesCallback
import com.viewnext.domain.repository.GetDetallesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio GetDetallesRepository. Esta clase se encarga de obtener los detalles
 * de una fuente remota (simulada por Retromock) y almacenarlos en una lista en caché.
 * También proporciona un métodos para obtener los detalles como un LiveData.
 */
@Singleton
class GetDetallesRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : GetDetallesRepository {

    private val apiServiceMock: ApiService =
        RetromockClient.getRetromockInstance(context).create(ApiService::class.java)

    private val detallesCache = mutableListOf<Detalles>()

    /**
     * Obtiene el LiveData que contiene la lista de detalles. El LiveData permite observar
     * los cambios en los detalles en tiempo real.
     * @return Un LiveData que contiene la lista de detalles.
     */
    private val detallesLiveData = MutableLiveData<List<Detalles>>()

    override fun getDetalles(): List<Detalles> {
        return detallesCache.toList()
    }

    /**
     * Realiza una solicitud a la API para obtener los detalles. Si la solicitud es exitosa,
     * se actualiza la caché y se pasa la lista de detalles al callback.
     * En caso de error, se pasa el error al callback.
     * @param callback El callback que maneja el éxito o el error al obtener los detalles.
     */
    override fun refreshDetalles(callback: DetallesCallback<List<Detalles>>) {
        apiServiceMock.detallesMock?.enqueue(object : Callback<DetallesResponse?> {

            override fun onResponse(
                call: Call<DetallesResponse?>,
                response: Response<DetallesResponse?>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    // Actualiza caché
                    detallesCache.clear()
                    detallesCache.addAll(body.detalles)

                    // Actualiza LiveData
                    detallesLiveData.value = detallesCache.toList()

                    // Notifica callback
                    callback.onSuccess(detallesCache.toList())

                    Log.d("Repository", "Detalles cargados: ${detallesCache.size}")
                } else {
                    // Manejo de error si la respuesta no es exitosa
                    callback.onFailure(Exception("Error al cargar detalles"))
                }
            }

            override fun onFailure(call: Call<DetallesResponse?>, t: Throwable) {
                // Manejo de error en fallo de red
                callback.onFailure(t)
            }
        })
    }

    fun getDetallesLiveData(): LiveData<List<Detalles>> = detallesLiveData
}