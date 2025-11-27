package com.example.projetopi.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentPersonInfoBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.util.concurrent.TimeUnit

class PersonInfoFragment : Fragment() {

    private var _binding: FragmentPersonInfoBinding? = null
    private val binding get() = _binding!!

    private val database = Firebase.database.reference
    private var userUid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userUid = arguments?.getString("user_uid")

        if (userUid.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Erro: UID de usuário ausente.", Toast.LENGTH_LONG).show()
            findNavController().popBackStack(R.id.authentication, true)
            return
        }

        setupDropdowns()
        initListeners()
    }

    private fun setupDropdowns() {
        val sexos = arrayOf("Feminino", "Masculino", "Não Binário", "Prefiro não informar")
        val sexoAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, sexos)
        binding.autoCompleteSexo.setAdapter(sexoAdapter)
    }

    private fun initListeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.profileImageContainer.setOnClickListener {
            Toast.makeText(requireContext(), "Abrir galeria ou câmera para foto de perfil.", Toast.LENGTH_SHORT).show()
        }

        binding.registerButton.setOnClickListener {
            val dadosPessoais = collectPersonalData()

            if (dadosPessoais != null) {
                salvarDadosPessoaFisica(dadosPessoais)
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun collectPersonalData(): Map<String, Any>? {
        val nome = binding.editTextNome.text.toString().trim()
        val cpf = binding.editTextCpf.text.toString().trim()
        val dataNasc = binding.editTextDataNasc.text.toString().trim()
        val sexo = binding.autoCompleteSexo.text.toString().trim()
        val telefone = binding.editTextTelefone.text.toString().trim()

        val fotoUrl = ""

        if (nome.isEmpty() || cpf.isEmpty() || dataNasc.isEmpty() || sexo.isEmpty() || telefone.isEmpty()) {
            return null
        }

        return mapOf(
            "cpf" to cpf,
            "fotoUrl" to fotoUrl,
            "nascimento" to dataNasc,
            "nome" to nome,
            "sexo" to sexo,
            "telefone" to telefone,
            "updatedAt" to TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        )
    }

    private fun salvarDadosPessoaFisica(dadosPessoais: Map<String, Any>) {
        val uid = userUid!!

        val finalUpdateMap = mapOf(
            "cadastro/PessoaFisica" to dadosPessoais
        )

        database.child("usuarios").child(uid).updateChildren(finalUpdateMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Cadastro Pessoa Física concluído!", Toast.LENGTH_LONG).show()

                findNavController().navigate(
                    R.id.action_global_socialFragment,
                    null,
                    androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.authentication, true)
                        .build()
                )
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Falha ao salvar dados pessoais: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}