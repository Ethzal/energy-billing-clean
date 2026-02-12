package com.viewnext.domain.usecase

import com.viewnext.domain.model.Factura
import com.viewnext.domain.repository.GetFacturasRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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
    fun `invoke should return success when repository returns success`() = runTest {
        // Arrange
        val fakeFacturas = listOf(
            Factura(/* tus datos fake */)
        )
        whenever(mockRepository.refreshFacturas(true))
            .thenReturn(Result.success(fakeFacturas))

        // Act
        val result = useCase(true)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(fakeFacturas, result.getOrNull())
        verify(mockRepository).refreshFacturas(true)
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Arrange
        val errorMsg = "Error de red"
        val exception = RuntimeException(errorMsg)
        whenever(mockRepository.refreshFacturas(false))
            .thenReturn(Result.failure(exception))

        // Act
        val result = useCase(false)

        // Assert
        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertEquals(errorMsg, error?.message)
        verify(mockRepository).refreshFacturas(false)
    }
}
