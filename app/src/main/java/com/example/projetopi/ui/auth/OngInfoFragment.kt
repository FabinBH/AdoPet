package com.example.projetopi.ui.auth

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
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

    private var encodedImage: String? = null
    private val imagePicker =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val uri = data?.data

                if (uri != null) {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    binding.profileImage.setImageBitmap(bitmap)
                    encodedImage = bitmapToBase64(bitmap)
                }
            }
        }

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
        setupMasks()
        initListeners()
    }

    private fun setupDropdowns() {
        val opcoesSimNao = arrayOf("Sim", "Não")

        binding.autoCompleteDoacoes.setAdapter(
            ArrayAdapter(requireContext(), R.layout.dropdown_item, opcoesSimNao)
        )

        binding.autoCompletePatrocinios.setAdapter(
            ArrayAdapter(requireContext(), R.layout.dropdown_item, opcoesSimNao)
        )
    }

    private fun setupMasks() {
        // Máscara CNPJ
        binding.editTextCnpj.addTextChangedListener(maskCNPJ(binding.editTextCnpj))

        // Máscara Telefone
        binding.editTextContato.addTextChangedListener(maskTelefone(binding.editTextContato))
    }

    private fun maskCNPJ(edit: android.widget.EditText) = object : TextWatcher {

        private var isUpdating = false

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) return

            val digits = s.toString().replace("[^0-9]".toRegex(), "")
            val limited = digits.take(14)

            val formatted = when {
                limited.length <= 2 -> limited
                limited.length <= 5 -> "${limited.substring(0, 2)}.${limited.substring(2)}"
                limited.length <= 8 -> "${limited.substring(0, 2)}.${limited.substring(2, 5)}.${limited.substring(5)}"
                limited.length <= 12 -> "${limited.substring(0, 2)}.${limited.substring(2, 5)}.${limited.substring(5, 8)}/${limited.substring(8)}"
                else -> "${limited.substring(0, 2)}.${limited.substring(2, 5)}.${limited.substring(5, 8)}/${limited.substring(8, 12)}-${limited.substring(12)}"
            }

            isUpdating = true
            edit.setText(formatted)
            edit.setSelection(formatted.length)
            isUpdating = false
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun maskTelefone(edit: android.widget.EditText) = object : TextWatcher {

        private var isUpdating = false

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (isUpdating) return

            val digits = s.toString().replace("[^0-9]".toRegex(), "")
            val limited = digits.take(11)

            val formatted = when {
                limited.length <= 2 ->
                    "(${limited}"
                limited.length <= 7 ->
                    "(${limited.substring(0, 2)}) ${limited.substring(2)}"
                limited.length <= 11 ->
                    "(${limited.substring(0, 2)}) ${limited.substring(2, 7)}-${limited.substring(7)}"
                else -> ""
            }

            isUpdating = true
            edit.setText(formatted)
            edit.setSelection(formatted.length)
            isUpdating = false
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun initListeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.profileImageContainer.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePicker.launch(intent)
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

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val bytes = outputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun collectOngDataPart1(): Map<String, Any>? {
        val nome = binding.editTextNome.text.toString().trim()
        val cnpj = binding.editTextCnpj.text.toString().trim()
        val doacoes = binding.autoCompleteDoacoes.text.toString().trim()
        val contato = binding.editTextContato.text.toString().trim()
        val patrocinio = binding.autoCompletePatrocinios.text.toString().trim()

        val fotoUrl = encodedImage ?: ""

        if (nome.isEmpty() || cnpj.isEmpty() || cnpj.length != 18 ||
            doacoes.isEmpty() || contato.isEmpty() || contato.length < 14 ||
            patrocinio.isEmpty()
        ) {
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
