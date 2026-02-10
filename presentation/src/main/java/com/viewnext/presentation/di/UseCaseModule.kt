package com.viewnext.presentation.di

import com.viewnext.domain.repository.GetDetallesRepository
import com.viewnext.domain.repository.GetFacturasRepository
import com.viewnext.domain.usecase.FilterFacturasUseCase
import com.viewnext.domain.usecase.GetDetallesUseCase
import com.viewnext.domain.usecase.GetFacturasUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Módulo de Hilt para proveer los casos de uso (UseCases) a los ViewModels.
 */
@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    /**
     * Provee un GetFacturasUseCase inyectando su repositorio correspondiente.
     */
    @Provides
    fun provideGetFacturasUseCase(
        repository: GetFacturasRepository
    ): GetFacturasUseCase {
        return GetFacturasUseCase(repository)
    }

    /**
     * Provee un FilterFacturasUseCase.
     * No requiere repositorio, ya que realiza filtrado local.
     */
    @Provides
    fun provideFilterFacturasUseCase(): FilterFacturasUseCase {
        return FilterFacturasUseCase()
    }

    /**
     * Provee un GetDetallesUseCase inyectando su repositorio correspondiente.
     */
    @Provides
    fun provideGetDetallesUseCase(
        repository: GetDetallesRepository
    ): GetDetallesUseCase {
        return GetDetallesUseCase(repository)
    }
}
