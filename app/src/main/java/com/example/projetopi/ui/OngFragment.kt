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
                OngAdapter.SELECT_DETAILS -> {
                    findNavController().navigate(R.id.ongDetailsFragment)
                }
                /*OngAdapter.SELECT_ADOPT -> {
                    findNavController().navigate(R.id.adoptionConfirmationFragment)
                }*/
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
                        val ong = cadastro.getValue(Ong::class.java)
                        if (ong?.nome != null) {
                            listaOngs.add(
                                Ong(
                                    id = usuarioSnapshot.key?.hashCode() ?: 0,
                                    nome = ong.nome,
                                    fotoUrl = ong.fotoUrl
                                )
                            )
                        }
                        /*val id = usuarioSnapshot.key ?: ""
                        val nome = cadastro.child("nome").getValue(String::class.java) ?: ""
                        val fotoUrl = cadastro.child("fotoUrl").getValue(String::class.java) ?: ""
                        lista.add(Ong(id, nome, fotoUrl))*/
                    }
                }

                adapter.submitList(listaOngs)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Erro ao carregar usuários", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
