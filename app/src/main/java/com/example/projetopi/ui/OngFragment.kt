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
import com.example.projetopi.data.model.Ong
import com.example.projetopi.databinding.FragmentOngBinding
import com.example.projetopi.ui.adapter.OngAdapter
import com.example.projetopi.ui.adapter.PetAdapter

class OngFragment : Fragment() {
    private var _binding: FragmentOngBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OngAdapter
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
        initListeners()

        recyclerView = view.findViewById(R.id.recyclerViewONG)

        adapter = OngAdapter(requireContext()) { ong, action ->
            when (action) {
                OngAdapter.SELECT_DETAILS -> {
                    Toast.makeText(requireContext(), "Detalhes do animal", Toast.LENGTH_SHORT).show()
                }
                OngAdapter.SELECT_ADOPT -> {
                    Toast.makeText(requireContext(), "Doar para ONG", Toast.LENGTH_SHORT).show()
                }
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val ongs = listOf(
            Ong(1, "Ampara"),
            Ong(2, "Pata"),
            Ong(3, "Eliabe")
        )

        adapter.submitList(ongs)
    }

    private fun initListeners() {
        binding.btnChat.setOnClickListener {
            findNavController().navigate(R.id.action_ongFragment_to_chatFragment)
        }
    }
}