package com.example.projetopi.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentChoosePersonBinding // Importe o binding gerado
import com.google.firebase.database.database
import com.google.firebase.Firebase

class ChoosePersonFragment : Fragment() {

    private var _binding: FragmentChoosePersonBinding? = null
    private val binding get() = _binding!!

    private val database = Firebase.database.reference
    private var userUid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoosePersonBinding.inflate(inflater, container, false)
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

        initListeners()
    }

    private fun initListeners() {
        val uid = userUid!!
        val bundle = Bundle().apply { putString("user_uid", uid) }

        binding.btnPessoa.setOnClickListener {
            salvarTipoPerfil(uid, "PessoaFisica") {
                findNavController().navigate(R.id.action_choosePersonFragment_to_personInfoFragment, bundle)
            }
        }

        binding.btnONG.setOnClickListener {
            salvarTipoPerfil(uid, "Instituicao") {
                findNavController().navigate(R.id.action_choosePersonFragment_to_ongInfoFragment, bundle)
            }
        }
    }

    private fun salvarTipoPerfil(uid: String, tipo: String, onSuccess: () -> Unit) {
        val updateMap = mapOf(
            "tipoDePerfil" to tipo
        )

        database.child(uid).updateChildren(updateMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Perfil '$tipo' selecionado.", Toast.LENGTH_SHORT).show()
                onSuccess()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Falha ao salvar o tipo de perfil: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}