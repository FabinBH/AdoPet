package com.example.projetopi.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.R
import com.example.projetopi.data.model.Animal
import com.example.projetopi.data.model.Pet
import com.example.projetopi.databinding.FragmentAdoptionBinding
import com.example.projetopi.ui.adapter.PetAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdoptionFragment : Fragment() {
    private var _binding: FragmentAdoptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PetAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private var allPetsList = mutableListOf<Pet>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdoptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewAdoption)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PetAdapter(requireContext()) { pet, action ->
            when (action) {
                PetAdapter.SELECT_ADOPT -> {
                    findNavController().navigate(R.id.action_adoptionFragment_to_adoptionConfirmationFragment)
                }
            }
        }

        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("animal_Cadastrado")

        carregarPets()
        initListeners()
        setupSearchListener()
    }

    private fun carregarPets() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allPetsList.clear()

                for (userSnap in snapshot.children) {
                    val cadastro = userSnap

                    if (cadastro.exists()) {
                        val pet = cadastro.getValue(Animal::class.java)
                        if (pet?.nome != null) {
                            allPetsList.add(
                                Pet(
                                    id = userSnap.key ?: "",
                                    nome = pet.nome,
                                    fotoUrl = pet.fotoUrl,
                                    descricao = pet.descricao,
                                    especie = pet.especie,
                                    idade = pet.idade,
                                    raca = pet.raca
                                )
                            )
                        }
                    }
                }

                adapter.submitList(allPetsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Erro ao carregar animais: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSearchListener() {
        binding.busca.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterList(query: String) {
        val lowerCaseQuery = query.lowercase().trim()

        if (lowerCaseQuery.isEmpty()) {
            adapter.submitList(allPetsList)
            return
        }

        val filteredList = allPetsList.filter { pet ->
            pet.nome?.lowercase()?.contains(lowerCaseQuery) == true ||
                    pet.especie?.lowercase()?.contains(lowerCaseQuery) == true ||
                    pet.raca?.lowercase()?.contains(lowerCaseQuery) == true
        }

        adapter.submitList(filteredList)

        if (filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "Nenhum pet encontrado para '$query'", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListeners() {
        binding.btnChat.setOnClickListener {
            findNavController().navigate(R.id.action_adoptionFragment_to_chatFragment)
        }
    }
}