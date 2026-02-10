package com.viewnext.presentation.di;

import com.viewnext.domain.repository.GetDetallesRepository;
import com.viewnext.domain.repository.GetFacturasRepository;
import com.viewnext.domain.usecase.FilterFacturasUseCase;
import com.viewnext.domain.usecase.GetDetallesUseCase;
import com.viewnext.domain.usecase.GetFacturasUseCase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

/**
 * Módulo de Hilt para proveer los casos de uso (UseCases) a los ViewModels.
 */
@Module
@InstallIn(ViewModelComponent.class)
public class UseCaseModule {

    /**
     * Provee un GetFacturasUseCase inyectando su repositorio correspondiente.
     */
    @Provides
    public static GetFacturasUseCase provideGetFacturasUseCase(
            GetFacturasRepository repository
    ) {
        return new GetFacturasUseCase(repository);
    }

    /**
     * Provee un FilterFacturasUseCase.
     * No requiere repositorio, ya que realiza filtrado local.
     */
    @Provides
    public static FilterFacturasUseCase provideFilterFacturasUseCase() {
        return new FilterFacturasUseCase();
    }

    /**
     * Provee un GetDetallesUseCase inyectando su repositorio correspondiente.
     */
    @Provides
    public static GetDetallesUseCase provideGetDetallesUseCase(
            GetDetallesRepository repository
    ) {
        return new GetDetallesUseCase(repository);
    }
}
