package com.example.projetopi.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentOngInfo2Binding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.util.concurrent.TimeUnit

class OngInfo2Fragment : Fragment() {

    private var _binding: FragmentOngInfo2Binding? = null
    private val binding get() = _binding!!

    private val database = Firebase.database.reference
    private var userUid: String? = null
    private var ongDataPart1: MutableMap<String, Any> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOngInfo2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userUid = arguments?.getString("user_uid")
        val dataString = arguments?.getString(OngInfoFragment.ONG_DATA_BUNDLE)

        if (userUid.isNullOrEmpty() || dataString.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Erro: Dados de cadastro ausentes.", Toast.LENGTH_LONG).show()
            findNavController().popBackStack(R.id.authentication, true)
            return
        }

        ongDataPart1 = stringToMap(dataString)

        setupViews()
        setupMasks()
        initListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        binding.titleText.text = "Informações de Contato e Necessidades"
    }

    private fun setupMasks() {
        binding.editTextTelefone.addTextChangedListener(TelefoneMask(binding.editTextTelefone))
    }

    private fun initListeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerButton.setOnClickListener {
            val dadosPart2 = collectOngDataPart2()

            if (dadosPart2 != null) {
                val dadosFinais = ongDataPart1.toMutableMap().apply {
                    putAll(dadosPart2)
                }
                salvarDadosFinais(dadosFinais)
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun collectOngDataPart2(): Map<String, Any>? {
        val email = binding.editTextEmail.text.toString().trim()
        val site = binding.editTextSite.text.toString().trim()
        val telefone = binding.editTextTelefone.text.toString().trim()
        val necessidade = binding.editTextNecessidade.text.toString().trim()

        if (email.isEmpty() || necessidade.isEmpty()) return null

        return mapOf(
            "email" to email,
            "site" to site,
            "telefoneContato2" to telefone,
            "necessidade" to necessidade
        )
    }

    private fun salvarDadosFinais(dadosFinais: Map<String, Any>) {
        val uid = userUid!!

        val finalData = dadosFinais.toMutableMap().apply {
            put("updatedAt", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()))
        }

        val finalUpdateMap = mapOf(
            "cadastro/PessoaJuridica" to finalData
        )

        database.child("usuarios").child(uid).updateChildren(finalUpdateMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Cadastro ONG finalizado com sucesso!", Toast.LENGTH_LONG).show()

                findNavController().navigate(
                    R.id.action_global_socialFragment,
                    null,
                    androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.authentication, true)
                        .build()
                )
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Falha ao finalizar cadastro da ONG: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun stringToMap(dataString: String): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        dataString.split(";").forEach { entry ->
            val parts = entry.split(":", limit = 2)
            if (parts.size == 2) {
                val key = parts[0]
                val value = parts[1]

                val finalValue: Any = when (value.lowercase()) {
                    "true" -> true
                    "false" -> false
                    else -> value
                }
                map[key] = finalValue
            }
        }
        return map
    }

    inner class TelefoneMask(private val editText: android.widget.EditText) : TextWatcher {

        private var isUpdating = false

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) return

            val digits = s.toString().replace("[^0-9]".toRegex(), "")
            val limited = digits.take(11)

            val formatted = when {
                limited.length <= 2 ->
                    "($limited"
                limited.length <= 7 ->
                    "(${limited.substring(0, 2)}) ${limited.substring(2)}"
                else ->
                    "(${limited.substring(0, 2)}) ${limited.substring(2, 7)}-${limited.substring(7)}"
            }

            isUpdating = true
            editText.setText(formatted)
            editText.setSelection(formatted.length)
            isUpdating = false
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
