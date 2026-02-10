package com.viewnext.domain.usecase

import com.viewnext.domain.model.Factura
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FilterFacturasUseCaseTest {
    private lateinit var useCase: FilterFacturasUseCase

    @Before
    fun setUp() {
        useCase = FilterFacturasUseCase()
    }

    @Test
    fun filtrarFacturas_sinFiltros_devuelveTodasLasFacturas() {
        // Arrange
        val facturas = crearFacturasDePrueba()
        val estadosSeleccionados: MutableList<String?>? = null

        // Act
        val resultado =
            useCase.filtrarFacturas(facturas, estadosSeleccionados, null, null, null, null)

        // Assert
        Assert.assertEquals("Debe devolver todas las facturas", 3, resultado.size.toLong())
    }

    @Test
    fun filtrarFacturas_conEstadoEspecifico_filtraCorrectamente() {
        // Arrange
        val facturas = crearFacturasDePrueba()
        val estadosSeleccionados = mutableListOf<String?>("PAGADA")

        // Act
        val resultado =
            useCase.filtrarFacturas(facturas, estadosSeleccionados, null, null, null, null)

        // Assert
        Assert.assertEquals("Debe devolver solo facturas PAGADA", 1, resultado.size.toLong())
        Assert.assertEquals("PAGADA", resultado[0]?.descEstado)
    }

    @Test
    fun filtrarFacturas_conRangoFechas_filtraCorrectamente() {
        // Arrange
        val facturas = crearFacturasDePrueba()

        // Act
        val resultado = useCase.filtrarFacturas(
            facturas, null, "01/01/2026", "15/01/2026", null, null
        )

        // Assert
        Assert.assertEquals("Debe devolver facturas en rango de fechas", 2, resultado.size.toLong())
    }

    @Test
    fun filtrarFacturas_conRangoImporte_filtraCorrectamente() {
        // Arrange
        val facturas = crearFacturasDePrueba()
        val importeMin = 150.0
        val importeMax = 200.0

        // Act
        val resultado =
            useCase.filtrarFacturas(facturas, null, null, null, importeMin, importeMax)

        // Assert
        Assert.assertEquals(
            "Debe devolver facturas en rango de importe",
            1,
            resultado.size.toLong()
        )
    }

    @Test
    fun filtrarFacturas_conTodosLosFiltros_filtraCorrectamente() {
        // Arrange
        val facturas = crearFacturasDePrueba()
        val estadosSeleccionados = mutableListOf<String?>("PAGADA")
        val importeMin = 100.0
        val importeMax = 200.0

        // Act
        val resultado = useCase.filtrarFacturas(
            facturas,
            estadosSeleccionados,
            null,
            null,
            importeMin,
            importeMax
        )

        // Assert
        Assert.assertEquals(
            "Debe devolver factura que cumpla todos los filtros",
            1,
            resultado.size.toLong()
        )
        Assert.assertEquals("PAGADA", resultado[0]?.descEstado)
    }

    @Test
    fun filtrarFacturas_listaVacia_devuelveListaVacia() {
        // Arrange
        val facturas: MutableList<Factura> = ArrayList()

        // Act
        val resultado = useCase.filtrarFacturas(facturas, null, null, null, null, null)

        // Assert
        Assert.assertTrue("Debe devolver lista vacía", resultado.isEmpty())
    }

    @Test
    fun filtrarFacturas_estadosVacios_devuelveTodasLasFacturas() {
        // Arrange
        val facturas = crearFacturasDePrueba()
        val estadosSeleccionados: MutableList<String?> = ArrayList() // Lista vacía

        // Act
        val resultado =
            useCase.filtrarFacturas(facturas, estadosSeleccionados, null, null, null, null)

        // Assert
        Assert.assertEquals(
            "Lista de estados vacía debe devolver todas las facturas",
            3,
            resultado.size.toLong()
        )
    }

    // Metodo de pruebas
    private fun crearFacturasDePrueba(): MutableList<Factura> {
        return mutableListOf(
            Factura(descEstado = "PENDIENTE", fecha = "10/01/2026", importeOrdenacion = 100.0),
            Factura(descEstado = "PAGADA", fecha = "12/01/2026", importeOrdenacion = 175.0),
            Factura(descEstado = "CANCELADA", fecha = "20/01/2026", importeOrdenacion = 250.0)
        )
    }
}
