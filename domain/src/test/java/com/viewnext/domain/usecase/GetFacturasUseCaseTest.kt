package com.viewnext.domain.usecase

import com.viewnext.domain.model.Factura
import com.viewnext.domain.repository.GetFacturasRepository
import com.viewnext.domain.repository.GetFacturasRepository.RepositoryCallback
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class GetFacturasUseCaseTest {
    @Mock
    private lateinit var mockRepository: GetFacturasRepository
    private lateinit var useCase: GetFacturasUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = GetFacturasUseCase(mockRepository)
    }

    @Test
    fun execute_true_callsRepositoryWithTrue() {
        // Arrange
        val callback = mock<GetFacturasUseCase.Callback>()
        val captor = argumentCaptor<RepositoryCallback>()

        // Act
        useCase.execute(true, callback)

        // Assert
        verify(mockRepository).refreshFacturas(eq(true), captor.capture())
        verifyNoMoreInteractions(mockRepository)

        // Simular que el repositorio devuelve éxito
        val facturasFake = mutableListOf<Factura>()
        captor.firstValue.onSuccess(facturasFake)

        verify(callback).onSuccess(facturasFake)
    }

    @Test
    fun execute_false_callsRepositoryWithFalse() {
        // Arrange
        val callback = mock<GetFacturasUseCase.Callback>()
        val captor = argumentCaptor<RepositoryCallback>()

        // Act
        useCase.execute(false, callback)

        // Assert
        verify(mockRepository).refreshFacturas(eq(false), captor.capture())
        verifyNoMoreInteractions(mockRepository)

        // Error
        val errorMsg = "Error fake"
        captor.firstValue.onError(RuntimeException(errorMsg))

        verify(callback).onError(errorMsg)
    }
}
