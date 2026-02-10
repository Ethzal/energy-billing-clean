package com.viewnext.presentation.ui.factura

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.viewnext.presentation.R

class FacturaNavigator(private val fragmentManager: FragmentManager) {
    fun openFilter(maxImporte: Float) {
        var filtroFragment =
            fragmentManager.findFragmentByTag("FILTRO_FRAGMENT") as? FiltroFragment

        if (filtroFragment == null) {
            filtroFragment = FiltroFragment()

            val args = Bundle()
            args.putFloat("MAX_IMPORTE", maxImporte)
            filtroFragment.setArguments(args)

            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            transaction.replace(R.id.fragment_container, filtroFragment, "FILTRO_FRAGMENT")
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    fun handleBackPressed(): Boolean {
        val filtroFragment =
            fragmentManager.findFragmentById(R.id.fragment_container) as FiltroFragment?

        if (filtroFragment != null && filtroFragment.isVisible) {
            fragmentManager.popBackStack()
            return true
        }
        return false
    }
}
