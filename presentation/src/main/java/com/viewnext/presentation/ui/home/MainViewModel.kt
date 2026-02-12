package com.viewnext.presentation.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel principal para MainActivity que gestiona el estado del toggle Retromock/Retrofit.
 * Funciones principales:
 * - Mantener estado reactivo del API activo (Retromock/Retrofit)
 * - Alternar entre APIs mediante toggle
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // Estado interno mutable
    private val _usingRetromock = MutableStateFlow(true)

    // Exposición inmutable
    val usingRetromock: StateFlow<Boolean> = _usingRetromock.asStateFlow()

    fun toggleApi() {
        _usingRetromock.value = !_usingRetromock.value
    }
}