package com.viewnext.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.viewnext.data.local.entity.FacturaEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de acceso a los datos de la tabla "facturas" en la base de datos.
 * Utiliza Room para facilitar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar).
 */
@Dao
interface FacturaDao {

    /**
     * Obtiene todas las facturas de la base de datos de manera asíncrona.
     * El resultado es devuelto como un [Flow], lo que permite
     * que cualquier cambio en la base de datos sea observado en tiempo real.
     *
     * @return [Flow] que contiene una lista de todas las facturas en la base de datos.
     */
    @Query("SELECT * FROM facturas")
    fun getFacturas(): Flow<List<FacturaEntity>>

    /**
     * Obtiene todas las facturas de la base de datos de manera síncrona.
     * Devuelve la lista completa de facturas sin observar cambios en tiempo real.
     *
     * @return Lista de facturas almacenadas en la base de datos.
     */
    @Query("SELECT * FROM facturas")
    fun getFacturasDirect(): List<FacturaEntity>

    /**
     * Inserta un conjunto de facturas en la base de datos.
     * Si alguna factura ya existe (mismo id), será reemplazada.
     *
     * @param facturas Lista de facturas a insertar o reemplazar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(facturas: List<FacturaEntity>)

    /**
     * Elimina todas las facturas de la base de datos.
     * Vacía la tabla "facturas".
     */
    @Query("DELETE FROM facturas")
    fun deleteAll()
}
