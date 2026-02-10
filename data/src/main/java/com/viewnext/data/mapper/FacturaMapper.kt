package com.viewnext.data.mapper

import com.viewnext.data.local.entity.FacturaEntity
import com.viewnext.domain.model.Factura

/**
 * Clase Mapper que se encarga de transformar los objetos entre el modelo de dominio [Factura] y
 * la entidad de la base de datos [FacturaEntity]. Los métodos de esta clase permiten convertir
 * entre estas representaciones, tanto a nivel individual como en listas completas.
 */
object FacturaMapper {
    /**
     * Convierte un objeto [Factura] en su correspondiente entidad [FacturaEntity].
     * @param factura El objeto de dominio [Factura] a convertir.
     * @return La entidad [FacturaEntity] que representa la factura en la base de datos.
     */
    fun toEntity(factura: Factura): FacturaEntity =
        FacturaEntity().apply {
            estado = factura.descEstado
            fecha = factura.fecha
            importe = factura.importeOrdenacion
            id = listOf(
                factura.descEstado,
                factura.fecha,
                factura.importeOrdenacion
            ).joinToString("|").hashCode()
        }

    /**
     * Convierte una lista de objetos [Factura] en una lista de entidades [FacturaEntity].
     * @param facturas La lista de objetos [Factura] a convertir.
     * @return La lista de entidades [FacturaEntity] correspondientes.
     */
    fun toEntityList(facturas: List<Factura>): List<FacturaEntity> =
        facturas.map { toEntity(it) }

    /**
     * Convierte una entidad [FacturaEntity] a su correspondiente objeto de dominio [Factura].
     * @param entity La entidad [FacturaEntity] a convertir.
     * @return El objeto de dominio [Factura] correspondiente.
     */
    fun toDomain(entity: FacturaEntity): Factura =
        Factura(
            descEstado = entity.estado,
            fecha = entity.fecha,
            importeOrdenacion = entity.importe
        )

    /**
     * Convierte una lista de entidades [FacturaEntity] en una lista de objetos de dominio [Factura].
     * @param entities La lista de entidades [FacturaEntity] a convertir.
     * @return La lista de objetos de dominio [Factura] correspondientes.
     */
    fun toDomainList(entities: List<FacturaEntity>): List<Factura> =
        entities.map { toDomain(it) }
}
