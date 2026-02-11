package com.viewnext.domain.usecase

import com.viewnext.domain.model.Detalles
import com.viewnext.domain.repository.DetallesCallback
import com.viewnext.domain.repository.GetDetallesRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.argumentCaptor


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
            override fun onSuccess(result: List<Detalles>) {}
            override fun onFailure(error: Throwable) {}
        })

        // Assert
        val captor = argumentCaptor<DetallesCallback<List<Detalles>>>()
        verify(mockRepository).refreshDetalles(captor.capture())
        verifyNoMoreInteractions(mockRepository)
    }
}