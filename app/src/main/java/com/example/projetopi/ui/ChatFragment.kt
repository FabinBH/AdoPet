package com.example.projetopi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.R
import com.example.projetopi.data.model.Chat
import com.example.projetopi.data.model.PessoaFisica
import com.example.projetopi.databinding.FragmentChatBinding
import com.example.projetopi.ui.adapter.ChatAdapter
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewChat)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ChatAdapter(requireContext()) { chat, action ->
            when (action) {
                ChatAdapter.SELECT_CHAT -> {
                    Toast.makeText(requireContext(), "Contato selecionado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("usuarios")

        carregarUsuariosPessoaFisica()
        initListener()
    }

    private fun carregarUsuariosPessoaFisica() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaChats = mutableListOf<Chat>()

                for (userSnap in snapshot.children) {
                    val pessoaFisicaSnap = userSnap.child("cadastro").child("PessoaFisica")

                    if (pessoaFisicaSnap.exists()) {
                        val pessoa = pessoaFisicaSnap.getValue(PessoaFisica::class.java)
                        if (pessoa?.nome != null) {
                            listaChats.add(
                                Chat(
                                    id = userSnap.key?.hashCode() ?: 0,
                                    nome = pessoa.nome,
                                    fotoBase64 = pessoa.fotoUrl,
                                    email = pessoa.email
                                )
                            )
                        }
                    }
                }

                adapter.submitList(listaChats)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Erro ao carregar usuários", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initListener() {
        binding.btnChat.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
