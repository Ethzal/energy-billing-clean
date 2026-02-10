package com.viewnext.presentation.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * Adapter para ViewPager2 que gestiona los fragmentos de SmartSolar.
 * Contiene tres pestañas: MiInstalacion, Energia y Detalles.
 */
public class SmartSolarPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragments;

    public SmartSolarPagerAdapter(
            @NonNull FragmentActivity fragmentActivity,
            @NonNull List<Fragment> fragments
    ) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    /**
     * Devuelve el fragmento correspondiente según la posición de la pestaña.
     * @param position Posición de la pestaña (0, 1, 2)
     * @return Fragment asociado a la pestaña
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    /**
     * Número total de pestañas/fragments en el ViewPager.
     * @return longitud del array de Fragments
     */
    @Override
    public int getItemCount() {
        return fragments.size(); // Número de pestañas
    }
}