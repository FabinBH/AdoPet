package com.example.projetopi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.R
import com.example.projetopi.data.model.Ong
import com.example.projetopi.data.model.PessoaJuridica
import com.example.projetopi.databinding.FragmentOngBinding
import com.example.projetopi.ui.adapter.OngAdapter
import com.google.firebase.database.*

class OngFragment : Fragment() {

    private var _binding: FragmentOngBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OngAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOngBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewONG)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = OngAdapter(requireContext()) { ong, action ->
            when (action) {
                OngAdapter.SELECT_ONG-> {
                    Toast.makeText(requireContext(), "ONG selecionada", Toast.LENGTH_SHORT).show()
                }
            }
        }

        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("usuarios")

        carregarOngs()
        initListeners()
    }

    private fun initListeners() {
        binding.btnChat.setOnClickListener {
            findNavController().navigate(R.id.action_ongFragment_to_chatFragment)
        }
    }

    private fun carregarOngs() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaOngs = mutableListOf<Ong>()

                for (usuarioSnapshot in snapshot.children) {
                    val cadastro = usuarioSnapshot.child("cadastro").child("PessoaJuridica")

                    if (cadastro.exists()) {
                        val ong = cadastro.getValue(PessoaJuridica::class.java)
                        if (ong?.instituicao != null) {
                            listaOngs.add(
                                Ong(
                                    id = usuarioSnapshot.key ?: "",
                                    nome = ong.instituicao,
                                    fotoUrl = ong.fotoUrl,
                                    cnpj = ong.cnpj,
                                    email = ong.email,
                                    telefone = ong.telefone
                                )
                            )
                        }
                    }
                }

                adapter.submitList(listaOngs)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Erro ao carregar ONGs", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
