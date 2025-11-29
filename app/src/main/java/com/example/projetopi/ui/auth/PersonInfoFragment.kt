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
import android.widget.EditText
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
        setupInputMasks()
        initListeners()
    }

    private fun setupDropdowns() {
        val sexos = arrayOf("Feminino", "Masculino", "Não Binário", "Prefiro não informar")
        val sexoAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, sexos)
        binding.autoCompleteSexo.setAdapter(sexoAdapter)
    }

    private fun setupInputMasks() {
        binding.editTextCpf.addTextChangedListener(CpfMask(binding.editTextCpf))
        binding.editTextDataNasc.addTextChangedListener(DateMask(binding.editTextDataNasc))
        binding.editTextTelefone.addTextChangedListener(PhoneMask(binding.editTextTelefone))
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

        binding.registerButton.setOnClickListener {
            val dadosPessoais = collectPersonalData()

            if (dadosPessoais != null) {
                salvarDadosPessoaFisica(dadosPessoais)
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val bytes = outputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun collectPersonalData(): Map<String, Any>? {
        val nome = binding.editTextNome.text.toString().trim()
        val cpf = binding.editTextCpf.text.toString().trim()
        val dataNasc = binding.editTextDataNasc.text.toString().trim()
        val telefone = binding.editTextTelefone.text.toString().trim()
        val sexo = binding.autoCompleteSexo.text.toString().trim()
        val cpfValido = cpf.length == 14
        val dataValida = dataNasc.length == 10
        val telefoneValido = telefone.length in 13..15

        if (nome.isEmpty() || !cpfValido || !dataValida || !telefoneValido || sexo.isEmpty()) {
            return null
        }

        return mapOf(
            "cpf" to cpf,
            "fotoUrl" to (encodedImage ?: ""),
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

    inner class DateMask(private val editText: EditText) : TextWatcher {
        private var isUpdating = false

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) return

            val digits = s.toString().replace("[^0-9]".toRegex(), "").take(8)

            var result = ""

            if (digits.isNotEmpty()) result += digits.substring(0, minOf(2, digits.length))
            if (digits.length >= 3) result += "/" + digits.substring(2, minOf(4, digits.length))
            if (digits.length >= 5) result += "/" + digits.substring(4, digits.length)

            isUpdating = true
            editText.setText(result)
            editText.setSelection(result.length)
            isUpdating = false
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    inner class CpfMask(private val editText: EditText) : TextWatcher {
        private var isUpdating = false
        private val mask = "###.###.###-##"

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) return

            var digits = s.toString().replace("[^0-9]".toRegex(), "")
            digits = digits.take(11)

            var result = ""
            var index = 0

            for (m in mask) {
                if (m == '#') {
                    if (index < digits.length) {
                        result += digits[index]
                        index++
                    }
                } else {
                    if (index < digits.length) result += m
                }
            }

            isUpdating = true
            editText.setText(result)
            editText.setSelection(result.length)
            isUpdating = false
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    inner class PhoneMask(private val editText: EditText) : TextWatcher {

        private var isUpdating = false

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) return

            val digits = s.toString().replace("[^0-9]".toRegex(), "")
            val limited = digits.take(11)

            val formatted = when {
                limited.length <= 2 -> {
                    "(${limited}"
                }
                limited.length <= 6 -> {
                    "(${limited.substring(0, 2)}) ${limited.substring(2)}"
                }
                limited.length <= 10 -> {
                    "(${limited.substring(0, 2)}) ${limited.substring(2, 6)}-${limited.substring(6)}"
                }
                else -> {
                    "(${limited.substring(0, 2)}) ${limited.substring(2, 7)}-${limited.substring(7)}"
                }
            }

            isUpdating = true
            editText.setText(formatted)
            editText.setSelection(formatted.length)
            isUpdating = false
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}
