package com.example.projetopi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.R
import com.example.projetopi.data.model.Pet
import com.example.projetopi.data.model.Status
import com.example.projetopi.databinding.FragmentOngBinding
import com.example.projetopi.ui.adapter.PetAdapter

class OngFragment : Fragment() {
    private var _binding: FragmentOngBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PetAdapter
    private lateinit var recyclerView: RecyclerView

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

        adapter = PetAdapter(requireContext()) { pet, action ->
            when (action) {
                PetAdapter.SELECT_DETAILS -> {
                    Toast.makeText(requireContext(), "Detalhes do animal", Toast.LENGTH_SHORT).show()
                }
                PetAdapter.SELECT_ADOPT -> {
                    Toast.makeText(requireContext(), "Doar para ONG", Toast.LENGTH_SHORT).show()
                }
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val pets = listOf(
            Pet(1, adopted = Status.NOT_ADOPTED),
            Pet(2, adopted = Status.NOT_ADOPTED),
            Pet(3, adopted = Status.NOT_ADOPTED)
        )

        adapter.submitList(pets)

        initListeners()
    }

    private fun initListeners() {
        binding.btnChat.setOnClickListener {
            findNavController().navigate(R.id.action_ongFragment_to_chatFragment)
        }
    }
}