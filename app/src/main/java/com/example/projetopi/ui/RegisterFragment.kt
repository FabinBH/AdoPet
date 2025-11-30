package com.example.projetopi.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.auth.auth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    private val animalsRef = database.child("animal_Cadastrado")

    private var currentOwnerUid: String? = null

    private var encodedImage: String? = null

    private val imagePicker =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val uri = data?.data

                if (uri != null) {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    binding.imageViewIcon.setImageBitmap(bitmap)
                    encodedImage = bitmapToBase64(bitmap)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentOwnerUid = auth.currentUser?.uid

        if (currentOwnerUid.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Erro: Usuário não autenticado. Faça login novamente.", Toast.LENGTH_LONG).show()
            return
        }

        setupDropdowns()
        initListeners()
    }

    private fun setupDropdowns() {
        val especies = arrayOf("Cão", "Gato", "Pássaro", "Roedor", "Outros")
        val especieAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, especies)
        binding.autoCompleteEspecie.setAdapter(especieAdapter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        binding.advanceButton.setOnClickListener {
            val dadosAnimal = collectAnimalData()

            if (dadosAnimal != null) {
                salvarDadosAnimal(dadosAnimal)
            } else {
                Toast.makeText(requireContext(), "Preencha a espécie, raça, idade e nome para continuar.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imageContainer.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePicker.launch(intent)
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val bytes = outputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun collectAnimalData(): Map<String, Any>? {
        val especie = binding.autoCompleteEspecie.text.toString().trim()
        val raca = binding.editTextRaca.text.toString().trim()
        val idadeStr = binding.editTextIdade.text.toString().trim()
        val nome = binding.editTextNome.text.toString().trim()
        val descricao = binding.editTextDescricao.text.toString().trim()
        val historico = binding.editTextHistorico.text.toString().trim()

        if (especie.isEmpty() || raca.isEmpty() || idadeStr.isEmpty() || nome.isEmpty()) {
            return null
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val timestamp = ZonedDateTime.now(ZoneId.of("UTC")).format(formatter)

        val fotoUrl = ""

        return mapOf(
            "donoUid" to currentOwnerUid!!,
            "nome" to nome,
            "especie" to especie,
            "raca" to raca,
            "idade" to idadeStr,
            "fotoUrl" to (encodedImage ?: ""),
            "descricao" to descricao,
            "historico" to historico,
            "fotoUrl" to fotoUrl,
            "disponivel" to true,
            "criadoEm" to timestamp
        )
    }

    private fun salvarDadosAnimal(dadosAnimal: Map<String, Any>) {
        val newAnimalRef = animalsRef.push()

        newAnimalRef.setValue(dadosAnimal)
            .addOnSuccessListener {
                val animalId = newAnimalRef.key
                Toast.makeText(requireContext(), "Animal ${dadosAnimal["nome"]} cadastrado com sucesso! ID: $animalId", Toast.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_registerFragment_to_animalRegisterFragment)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Falha ao cadastrar animal: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}