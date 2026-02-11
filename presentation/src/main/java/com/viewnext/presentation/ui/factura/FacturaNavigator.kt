package com.viewnext.presentation.ui.factura

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.viewnext.presentation.R

class FacturaNavigator(private val fragmentManager: FragmentManager) {
    fun openFilter(maxImporte: Float) {
        val tag = "FILTRO_FRAGMENT"

        val filtroFragment = fragmentManager.findFragmentByTag(tag) as? FiltroFragment
            ?: FiltroFragment().apply {
                arguments = Bundle().apply {
                    putFloat("MAX_IMPORTE", maxImporte)
                }
            }

        // Solo reemplazamos si no está ya agregado
        if (filtroFragment.isAdded.not()) {
            fragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                replace(R.id.fragment_container, filtroFragment, tag)
                addToBackStack(null)
                commit()
            }
        }
    }

    fun handleBackPressed(): Boolean {
        val filtroFragment = fragmentManager.findFragmentById(R.id.fragment_container) as? FiltroFragment
        return if (filtroFragment?.isVisible == true) {
            fragmentManager.popBackStack()
            true
        } else {
            false
        }
    }
}
