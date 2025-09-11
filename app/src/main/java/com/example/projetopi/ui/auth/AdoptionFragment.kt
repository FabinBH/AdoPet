package com.example.projetopi.ui.auth

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentAdoptionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdoptionFragment : Fragment() {
    private var _binding: FragmentAdoptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdoptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val specialItem = bottomNav.menu.findItem(R.id.nav_register)
        val colorStateList = ColorStateList.valueOf(Color.parseColor("#FE8C00"))

        specialItem.iconTintList = colorStateList

        initListeners()
    }

    private fun initListeners() {
        binding.btnAdotar1.setOnClickListener {
            findNavController().navigate(R.id.adoptionConfirmationFragment)
        }
        binding.btnAdotar2.setOnClickListener {
            findNavController().navigate(R.id.adoptionConfirmationFragment)
        }
        binding.imageViewFiltro.setOnClickListener {
            findNavController().navigate(R.id.filterFragment)
        }
    }
}