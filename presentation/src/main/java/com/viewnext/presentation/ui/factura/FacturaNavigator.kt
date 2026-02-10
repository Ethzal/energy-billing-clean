package com.viewnext.presentation.ui.factura;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.viewnext.presentation.R;

public class FacturaNavigator {

    private final FragmentManager fragmentManager;

    public FacturaNavigator(@NonNull FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void openFilter(float maxImporte) {
        FiltroFragment filtroFragment =
                (FiltroFragment) fragmentManager.findFragmentByTag("FILTRO_FRAGMENT");

        if (filtroFragment == null) {
            filtroFragment = new FiltroFragment();

            Bundle args = new Bundle();
            args.putFloat("MAX_IMPORTE", maxImporte);
            filtroFragment.setArguments(args);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
            transaction.replace(R.id.fragment_container, filtroFragment, "FILTRO_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public boolean handleBackPressed() {
        FiltroFragment filtroFragment =
                (FiltroFragment) fragmentManager.findFragmentById(R.id.fragment_container);

        if (filtroFragment != null && filtroFragment.isVisible()) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }
}
