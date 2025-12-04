package com.example.projetopi.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseDatabase.getInstance().reference }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carregarDados()
        configurarLogout()
    }

    @SuppressLint("SetTextI18n")
    private fun carregarDados() {
        val user = auth.currentUser
        val uid = user?.uid

        if (uid.isNullOrEmpty()) {
            binding.textViewNome.text = "Erro de Autenticação"
            binding.textViewEmail.text = "Faça login novamente"
            Toast.makeText(requireContext(), "Nenhum usuário logado.", Toast.LENGTH_LONG).show()
            return
        }

        binding.textViewEmail.text = user.email ?: "E-mail não disponível"

        val userRef = db.child("usuarios").child(uid).child("cadastro")

        userRef.get()
            .addOnSuccessListener { snapshot ->
                var nomeUsuario: String? = snapshot.child("PessoaFisica").child("nome").value as? String

                if (nomeUsuario.isNullOrEmpty()) {
                    nomeUsuario = snapshot.child("PessoaJuridica").child("nome").value as? String
                }

                if (!nomeUsuario.isNullOrEmpty()) {
                    binding.textViewNome.text = nomeUsuario
                } else {
                    binding.textViewNome.text = user.displayName ?: "Nome não cadastrado"
                }
            }
            .addOnFailureListener {
                Log.e("ProfileFragment", "Erro ao carregar dados do usuário no Realtime DB", it)
                Toast.makeText(requireContext(), "Falha ao carregar dados: ${it.message}", Toast.LENGTH_LONG).show()
                binding.textViewNome.text = user.displayName ?: "Erro de Rede/DB"
            }
    }

    private fun configurarLogout() {
        binding.buttonLogout.setOnClickListener {
            try {
                auth.signOut()
                Toast.makeText(requireContext(), "Sessão encerrada com sucesso.", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_profileFragment_to_authentication2)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao sair: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}