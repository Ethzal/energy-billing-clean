package com.viewnext.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una factura almacenada en la base de datos local.
 * Esta clase se mapea a la tabla "facturas" de la base de datos utilizando Room.
 * @see FacturaEntity
 */
@Entity(tableName = "facturas")
class FacturaEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var estado: String = "Sin estado"
    var fecha: String = ""
    var importe: Double = 0.0
}
