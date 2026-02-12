package com.viewnext.domain.usecase

import com.viewnext.domain.model.Detalles
import com.viewnext.domain.repository.GetDetallesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetDetallesUseCaseTest {

    @Mock
    private lateinit var mockRepository: GetDetallesRepository

    private lateinit var useCase: GetDetallesUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = GetDetallesUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success when repository returns success`() = runTest {
        // Arrange
        val fakeList = emptyList<Detalles>()
        whenever(mockRepository.refreshDetalles())
            .thenReturn(Result.success(fakeList))

        // Act
        val result = useCase()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(fakeList, result.getOrNull())
        verify(mockRepository).refreshDetalles()
    }
}
