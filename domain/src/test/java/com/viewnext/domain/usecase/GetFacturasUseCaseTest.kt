package com.viewnext.domain.usecase

import com.viewnext.domain.model.Factura
import com.viewnext.domain.repository.GetFacturasRepository
import com.viewnext.domain.repository.GetFacturasRepository.RepositoryCallback
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations

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
        val callback =
            Mockito.mock<GetFacturasUseCase.Callback>(GetFacturasUseCase.Callback::class.java)

        val captor =
            ArgumentCaptor.forClass(RepositoryCallback::class.java)

        // Act
        useCase.execute(true, callback)

        // Assert
        verify(mockRepository).refreshFacturas(eq(true), captor.capture())
        verifyNoMoreInteractions(mockRepository)

        // Simular que el repositorio devuelve éxito
        val facturasFake = mutableListOf<Factura>()
        captor.value?.onSuccess(facturasFake)

        verify(callback).onSuccess(facturasFake)
    }

    @Test
    fun execute_false_callsRepositoryWithFalse() {
        // Arrange
        val callback =
            Mockito.mock<GetFacturasUseCase.Callback>(GetFacturasUseCase.Callback::class.java)
        val captor =
            ArgumentCaptor.forClass(RepositoryCallback::class.java)

        // Act
        useCase.execute(false, callback)

        // Assert
        verify(mockRepository).refreshFacturas(eq(false), captor.capture())
        verifyNoMoreInteractions(mockRepository)

        // Error
        val errorMsg = "Error fake"
        captor.value?.onError(RuntimeException(errorMsg))

        verify(callback).onError(errorMsg)
    }
}
