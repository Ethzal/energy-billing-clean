package com.viewnext.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter para ViewPager2 que gestiona los fragmentos de SmartSolar.
 * Contiene tres pestañas: MiInstalacion, Energia y Detalles.
 */
class SmartSolarPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: MutableList<Fragment>
) : FragmentStateAdapter(fragmentActivity) {
    /**
     * Devuelve el fragmento correspondiente según la posición de la pestaña.
     * @param position Posición de la pestaña (0, 1, 2)
     * @return Fragment asociado a la pestaña
     */
    override fun createFragment(position: Int) = fragments[position]

    /**
     * Número total de pestañas/fragments en el ViewPager.
     * @return longitud del array de Fragments
     */
    override fun getItemCount() = fragments.size // Número de pestañas
}