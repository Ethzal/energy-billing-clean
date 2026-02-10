package com.viewnext.presentation.ui.smartsolar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.viewnext.presentation.databinding.FragmentMiInstalacionBinding

/**
 * Fragment que muestra la sección "Mi Instalación" de Smart Solar.
 * Utiliza ViewBinding para acceder a los elementos de layout de manera segura.
 */
class MiInstalacionFragment : Fragment() {
    private var _binding: FragmentMiInstalacionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMiInstalacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}