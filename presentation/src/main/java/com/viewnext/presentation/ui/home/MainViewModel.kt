package com.viewnext.presentation.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel principal para MainActivity que gestiona el estado del toggle Retromock/Retrofit.
 * Funciones principales:
 * - Mantener estado reactivo del API activo (Retromock/Retrofit)
 * - Alternar entre APIs mediante toggle
 * - Exponer LiveData para UI reactiva
 */
@HiltViewModel
public class MainViewModel extends ViewModel {
    private final MutableLiveData<Boolean> usingRetromock = new MutableLiveData<>(true);

    @Inject
    public MainViewModel() {
        // Constructor vacío para Hilt
    }
    public LiveData<Boolean> getUsingRetromock() { return usingRetromock; }

    // Cambiar entre Retromock y Retrofit
    public void toggleApi() {
        boolean current = usingRetromock.getValue() != null ? usingRetromock.getValue() : true;
        usingRetromock.setValue(!current);
    }
}
