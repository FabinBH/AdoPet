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
import com.example.projetopi.databinding.FragmentOngInfoBinding
import java.util.concurrent.TimeUnit

class OngInfoFragment : Fragment() {

    private var _binding: FragmentOngInfoBinding? = null
    private val binding get() = _binding!!

    private var userUid: String? = null

    companion object {
        const val ONG_DATA_BUNDLE = "ong_data_bundle"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOngInfoBinding.inflate(inflater, container, false)
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
        val opcoesSimNao = arrayOf("Sim", "Não")

        val doacoesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, opcoesSimNao)
        val patrociniosAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, opcoesSimNao)

        binding.autoCompleteDoacoes.setAdapter(doacoesAdapter)
        binding.autoCompletePatrocinios.setAdapter(patrociniosAdapter)
    }

    private fun initListeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.profileImageContainer.setOnClickListener {
            Toast.makeText(requireContext(), "Abrir galeria ou câmera para o logo da ONG.", Toast.LENGTH_SHORT).show()
        }

        binding.advanceButton.setOnClickListener {
            val dadosParciais = collectOngDataPart1()

            if (dadosParciais != null) {
                navigateToNextStep(dadosParciais)
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos obrigatórios da ONG.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun collectOngDataPart1(): Map<String, Any>? {
        val nome = binding.editTextNome.text.toString().trim()
        val cnpj = binding.editTextCnpj.text.toString().trim()
        val doacoes = binding.autoCompleteDoacoes.text.toString().trim()
        val contato = binding.editTextContato.text.toString().trim()
        val patrocinio = binding.autoCompletePatrocinios.text.toString().trim()

        val fotoUrl = ""

        if (nome.isEmpty() || cnpj.isEmpty() || doacoes.isEmpty() || contato.isEmpty() || patrocinio.isEmpty()) {
            return null
        }

        return mapOf(
            "nome" to nome,
            "cnpj" to cnpj,
            "aceitaDoacoes" to (doacoes == "Sim"),
            "contato" to contato,
            "patrociniosConvenios" to (patrocinio == "Sim"),
            "fotoUrl" to fotoUrl,
            "updatedAt" to TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        )
    }

    private fun navigateToNextStep(dadosParciais: Map<String, Any>) {
        val uid = userUid!!

        val bundle = Bundle().apply {
            putString("user_uid", uid)
            putString(ONG_DATA_BUNDLE, mapToString(dadosParciais))
        }

        findNavController().navigate(R.id.action_ongInfoFragment_to_ongInfo2Fragment, bundle)
    }

    private fun mapToString(data: Map<String, Any>): String {
        return data.entries.joinToString(separator = ";") { "${it.key}:${it.value}" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}