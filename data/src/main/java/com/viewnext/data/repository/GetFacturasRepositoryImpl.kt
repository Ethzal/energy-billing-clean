package com.viewnext.data.repository

import android.content.Context
import com.viewnext.data.api.ApiService
import com.viewnext.data.api.RetrofitClient
import com.viewnext.data.api.RetromockClient
import com.viewnext.data.local.AppDatabase
import com.viewnext.data.local.dao.FacturaDao
import com.viewnext.data.mapper.FacturaMapper
import com.viewnext.domain.model.Factura
import com.viewnext.domain.repository.GetFacturasRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
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

    override fun getFacturas(): Flow<List<Factura>> =
        facturaDao.getFacturas().map { FacturaMapper.toDomainList(it) }

    /**
     * Refresca las facturas desde la API (Retrofit o Retromock) y las guarda en Room.
     */
    override suspend fun refreshFacturas(usingRetromock: Boolean): Result<List<Factura>> =
        withContext(Dispatchers.IO) {
            try {
                val apiService = if (usingRetromock) apiServiceMock else apiServiceRetrofit
                val call = if (usingRetromock) apiService.facturasMock else apiService.facturas

                val response = call?.awaitResponse()
                if (response?.isSuccessful == true && response.body() != null) {
                    val body = response.body()!!
                    val entities = FacturaMapper.toEntityList(body.facturas)
                    facturaDao.deleteAll()
                    facturaDao.insertAll(entities)
                    Result.success(FacturaMapper.toDomainList(entities))
                } else {
                    Result.success(getFacturasSync())
                }
            } catch (_: Throwable) {
                Result.success(getFacturasSync())
            }
        }

    /**
     * Carga las facturas directamente desde Room si falla la API.
     */
    private suspend fun getFacturasSync(): List<Factura> =
        withContext(Dispatchers.IO) {
            FacturaMapper.toDomainList(facturaDao.getFacturasDirect())
        }

    /**
     * Obtiene la lista de facturas desde Room de forma sincrónica.
     */
    fun getFacturasFromDb(): List<Factura> {
        val entities = facturaDao.getFacturasDirect()
        return FacturaMapper.toDomainList(entities)
    }
}
