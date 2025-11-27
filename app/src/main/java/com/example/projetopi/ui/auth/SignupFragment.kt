package com.example.projetopi.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
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
        binding.buttonEntrar.setOnClickListener {
            val username = binding.etUser.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etSenha.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUp(username, email, password)
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment3)
        }

        binding.imageButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun signUp(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid

                    if (uid != null) {

                        salvarDadosUsuario(uid, username, email)

                        Toast.makeText(requireContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

                        val bundle = Bundle().apply {
                            putString("user_uid", uid)
                            putString("user_email", email)
                        }

                        findNavController().navigate(R.id.action_signupFragment2_to_locateFragment, bundle)

                    } else {
                        Toast.makeText(requireContext(), "Erro: UID não encontrado.", Toast.LENGTH_LONG).show()
                    }

                } else {
                    Toast.makeText(requireContext(), task.exception?.message ?: "Erro no cadastro", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun salvarDadosUsuario(uid: String, username: String, email: String) {
        val dbRootRef = db.getReference()

        val userNode = dbRootRef.child("usuarios").child(uid)

        val userData = hashMapOf(
            "email" to email,
            "username" to username,
            "createdAt" to System.currentTimeMillis()
        )

        userNode.setValue(userData)
            .addOnSuccessListener {
                Log.d("SignupFragment", "Dados básicos salvos com sucesso em /usuarios/$uid")
            }
            .addOnFailureListener { e ->
                Log.e("SignupFragment", "Falha ao salvar dados básicos: ${e.message}", e)
                Toast.makeText(requireContext(), "Falha ao salvar dados básicos.", Toast.LENGTH_SHORT).show()
            }
    }
}