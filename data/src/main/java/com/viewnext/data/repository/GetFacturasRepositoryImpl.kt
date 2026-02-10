package com.viewnext.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.viewnext.data.api.ApiService
import com.viewnext.data.api.RetrofitClient
import com.viewnext.data.api.RetromockClient
import com.viewnext.data.local.AppDatabase
import com.viewnext.data.local.dao.FacturaDao
import com.viewnext.data.local.entity.FacturaEntity
import com.viewnext.data.mapper.FacturaMapper
import com.viewnext.domain.model.Factura
import com.viewnext.domain.model.FacturaResponse
import com.viewnext.domain.repository.GetFacturasRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio GetFacturasRepository en Kotlin.
 * Gestiona la obtención de facturas desde Room, Retrofit y Retromock.
 */
@Singleton
class GetFacturasRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : GetFacturasRepository {

    private val apiServiceRetrofit: ApiService = RetrofitClient.apiService
    private val apiServiceMock: ApiService = RetromockClient.getRetromockInstance(context)
        .create(ApiService::class.java)
    private val facturaDao: FacturaDao = AppDatabase.getInstance(context).facturaDao()

    override fun getFacturas(): List<Factura> {
        val entities = facturaDao.getFacturasDirect()
        return FacturaMapper.toDomainList(entities)
    }

    /**
     * Devuelve un LiveData que observa la lista de facturas en Room.
     */
    fun getFacturasLiveData(): LiveData<List<Factura>> {
        return object : LiveData<List<Factura>>() {
            private val observer = Observer<List<FacturaEntity>> { entities ->
                value = FacturaMapper.toDomainList(entities)
            }

            override fun onActive() {
                super.onActive()
                facturaDao.getFacturas().observeForever(observer)
            }

            override fun onInactive() {
                super.onInactive()
                facturaDao.getFacturas().removeObserver(observer)
            }
        }
    }


    /**
     * Refresca las facturas desde la API (Retrofit o Retromock) y las guarda en Room.
     */
    override fun refreshFacturas(
        usingRetromock: Boolean,
        callback: GetFacturasRepository.RepositoryCallback
    ) {
        val apiService = if (usingRetromock) apiServiceMock else apiServiceRetrofit
        val call: Call<FacturaResponse?>? = if (usingRetromock) {
            apiService.facturasMock
        } else {
            apiService.facturas
        }

        call?.enqueue(object : Callback<FacturaResponse?> {
            override fun onResponse(
                call: Call<FacturaResponse?>,
                response: Response<FacturaResponse?>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Executors.newSingleThreadExecutor().execute {
                        val entities = FacturaMapper.toEntityList(body.facturas)
                        facturaDao.deleteAll()
                        facturaDao.insertAll(entities)
                        callback.onSuccess(FacturaMapper.toDomainList(entities))
                    }
                } else {
                    loadFromRoom(callback)
                }
            }

            override fun onFailure(call: Call<FacturaResponse?>, t: Throwable) {
                loadFromRoom(callback)
            }
        })
    }

    /**
     * Carga las facturas directamente desde Room si falla la API.
     */
    private fun loadFromRoom(callback: GetFacturasRepository.RepositoryCallback) {
        Executors.newSingleThreadExecutor().execute {
            val entities = facturaDao.getFacturasDirect()
            callback.onSuccess(FacturaMapper.toDomainList(entities))
        }
    }

    /**
     * Obtiene la lista de facturas desde Room de forma sincrónica.
     */
    fun getFacturasFromDb(): List<Factura> {
        val entities = facturaDao.getFacturasDirect()
        return FacturaMapper.toDomainList(entities)
    }
}
