package com.viewnext.energyapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase principal de la aplicación para inicializar el framework de inyección de dependencias Hilt.
 * Esta clase está anotada con @HiltAndroidApp para activar la generación de código de Hilt,
 * lo que hace que la aplicación esté lista para la inyección de dependencias a lo largo del ciclo de vida de la app.
 */
@HiltAndroidApp
class EnergyApp : Application()
