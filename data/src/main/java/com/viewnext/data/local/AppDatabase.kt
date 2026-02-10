package com.viewnext.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.viewnext.data.local.dao.FacturaDao
import com.viewnext.data.local.entity.FacturaEntity

/**
 * Base de datos Room para la aplicación. Esta clase define la base de datos
 * y proporciona acceso a las entidades y DAO (Data Access Objects).
 * @see FacturaDao
 * @see FacturaEntity
 */
@Database(entities = [FacturaEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al DAO para las operaciones con las facturas.
     * @return Un objeto [FacturaDao] para acceder a la base de datos de facturas.
     */
    abstract fun facturaDao(): FacturaDao

    companion object {
        // Instancia singleton de la base de datos
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos.
         * Utiliza el patrón Singleton para asegurar que solo haya una instancia de la base de datos
         * durante el ciclo de vida de la aplicación.
         * @param context El contexto de la aplicación, utilizado para crear la base de datos.
         * @return La instancia única de [AppDatabase].
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
