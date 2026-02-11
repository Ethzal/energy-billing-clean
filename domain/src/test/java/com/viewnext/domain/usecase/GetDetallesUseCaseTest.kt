package com.viewnext.domain.usecase

import com.viewnext.domain.model.Detalles
import com.viewnext.domain.repository.DetallesCallback
import com.viewnext.domain.repository.GetDetallesRepository
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations

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
    fun refreshDetalles_callsRepositoryRefresh() {
        // Arrange
        // @Before

        // Act
        useCase.refreshDetalles(object : DetallesCallback<List<Detalles>> {
            override fun onSuccess(result: List<Detalles>) {
            }

            override fun onFailure(error: Throwable) {
            }
        })

        // Assert
        verify(mockRepository).refreshDetalles(any())
        verifyNoMoreInteractions(mockRepository)
    }
}