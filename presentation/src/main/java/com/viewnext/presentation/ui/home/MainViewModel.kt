package com.viewnext.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel principal para MainActivity que gestiona el estado del toggle Retromock/Retrofit.
 * Funciones principales:
 * - Mantener estado reactivo del API activo (Retromock/Retrofit)
 * - Alternar entre APIs mediante toggle
 * - Exponer LiveData para UI reactiva
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    val usingRetromock = MutableLiveData<Boolean?>(true)

    fun getUsingRetromock(): LiveData<Boolean?> {
        return usingRetromock
    }

    // Cambiar entre Retromock y Retrofit
    fun toggleApi() {
        val current = usingRetromock.value ?: true
        usingRetromock.value = !current
    }
}
