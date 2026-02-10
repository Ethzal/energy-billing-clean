package com.viewnext.data.di

import com.viewnext.data.repository.GetDetallesRepositoryImpl
import com.viewnext.data.repository.GetFacturasRepositoryImpl
import com.viewnext.domain.repository.GetDetallesRepository
import com.viewnext.domain.repository.GetFacturasRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Dagger para la inyección de dependencias de los repositorios.
 * Este módulo se encarga de vincular las implementaciones concretas de los repositorios con sus interfaces
 * para que Dagger pueda inyectarlas en los lugares necesarios.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Vincula la implementación [GetFacturasRepositoryImpl] con la interfaz [GetFacturasRepository].
     * Esto permite que Dagger proporcione la implementación concreta siempre que se requiera un [GetFacturasRepository].
     *
     * @param impl Implementación de [GetFacturasRepository] que se inyectará.
     * @return Una instancia de [GetFacturasRepository] proporcionada por Dagger.
     */
    @Binds
    @Singleton
    abstract fun bindGetFacturasRepository(impl: GetFacturasRepositoryImpl): GetFacturasRepository

    /**
     * Vincula la implementación [GetDetallesRepositoryImpl] con la interfaz [GetDetallesRepository].
     * Esto permite que Dagger proporcione la implementación concreta siempre que se requiera un [GetDetallesRepository].
     *
     * @param impl Implementación de [GetDetallesRepository] que se inyectará.
     * @return Una instancia de [GetDetallesRepository] proporcionada por Dagger.
     */
    @Binds
    @Singleton
    abstract fun bindGetDetallesRepository(impl: GetDetallesRepositoryImpl): GetDetallesRepository
}
