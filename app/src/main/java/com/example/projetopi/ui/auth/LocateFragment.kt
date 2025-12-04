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
import com.example.projetopi.databinding.FragmentLocateBinding
import com.example.projetopi.ui.util.CepMask
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.util.concurrent.TimeUnit

class LocateFragment : Fragment() {

    private var _binding: FragmentLocateBinding? = null
    private val binding get() = _binding!!

    private val database = Firebase.database.reference

    private var userUid: String? = null
    private var userEmail: String? = null

    private val dadosLocalizacao = mapOf(
        "Espírito Santo" to listOf("Vitória", "Vila Velha", "Serra", "Cariacica", "Guarapari"),
        "São Paulo" to listOf("São Paulo", "Campinas", "Santos", "Ribeirão Preto", "Guarulhos"),
        "Rio de Janeiro" to listOf("Rio de Janeiro", "Niterói", "Duque de Caxias", "Petrópolis"),
        "Minas Gerais" to listOf("Belo Horizonte", "Contagem", "Uberlândia", "Juiz de Fora")
    )
    private val estados = dadosLocalizacao.keys.toTypedArray()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userUid = arguments?.getString("user_uid")
        userEmail = arguments?.getString("user_email")

        if (userUid.isNullOrEmpty() || userEmail.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Erro: Dados de autenticação ausentes.", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
            return
        }

        setupDropdowns()
        binding.cepEditText.addTextChangedListener(CepMask(binding.cepEditText))
        initListeners()
    }

    private fun setupDropdowns() {
        val estadoAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, estados)
        binding.stateAutoCompleteTextView.setAdapter(estadoAdapter)

        binding.stateAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val estadoSelecionado = parent.getItemAtPosition(position).toString()

            val cidades = dadosLocalizacao[estadoSelecionado]?.toTypedArray() ?: emptyArray()

            val cidadeAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, cidades)
            binding.cityAutoCompleteTextView.setAdapter(cidadeAdapter)

            binding.cityAutoCompleteTextView.setText("", false)
            binding.cityAutoCompleteTextView.requestFocus()
        }

        binding.cityAutoCompleteTextView.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_item, emptyArray<String>()))
    }

    private fun initListeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerButton.setOnClickListener {
            val cep = binding.cepEditText.text.toString().trim()
            val estado = binding.stateAutoCompleteTextView.text.toString().trim()
            val cidade = binding.cityAutoCompleteTextView.text.toString().trim()

            if (cep.length != 9 || estado.isEmpty() || cidade.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos da localização.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dadosLocalizacao = mapOf<String, Any>(
                "cep" to cep,
                "estado" to estado,
                "cidade" to cidade,
                "updatedAt" to TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
            )
            salvarDadosLocalizacao(dadosLocalizacao)
        }
    }

    private fun salvarDadosLocalizacao(dados: Map<String, Any>) {
        val uid = userUid!!

        val updateMap = mutableMapOf(
            "localizacao" to dados,
            "email" to userEmail.toString(),
            "createdAt" to System.currentTimeMillis()
        )

        database.child("usuarios").child(uid).updateChildren(updateMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Localização registrada!", Toast.LENGTH_SHORT).show()

                val bundle = Bundle().apply { putString("user_uid", uid) }
                findNavController().navigate(R.id.action_locateFragment_to_choosePersonFragment, bundle)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Falha ao registrar dados: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}