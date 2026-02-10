package com.viewnext.presentation.ui.smartsolar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.viewnext.presentation.databinding.FragmentEnergiaBinding

/**
 * Fragment que representa la sección "Energía" dentro de Smart Solar.
 * Se encarga de mostrar la interfaz relacionada con la energía generada/consumida
 * y utiliza ViewBinding para acceder de manera segura a los elementos de layout.
 */
class EnergiaFragment : Fragment() {
    private var _binding: FragmentEnergiaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnergiaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}