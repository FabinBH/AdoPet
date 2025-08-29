package com.example.projetopi.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.imageButton.setOnClickListener {
            findNavController().navigate(R.id.dashboardFragment)
        }
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.adoptionFragment)
        }
        binding.criarconta.setOnClickListener {
            findNavController().navigate(R.id.signupFragment2)
        }
        // Tela de "Esqueceu a senha" precisa ser criada
        //binding.textView3.setOnClickListener {
        //    findNavController().navigate(R.id.forgotPasswordFragment)
        //}
    }
}