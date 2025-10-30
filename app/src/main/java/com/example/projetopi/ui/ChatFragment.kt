package com.example.projetopi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentChatBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.view.get
import androidx.core.view.size
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.data.model.Chat
import com.example.projetopi.ui.adapter.ChatAdapter

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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

        adapter = ChatAdapter(requireContext()) { chat, action ->
            when (action) {
                ChatAdapter.SELECT_CHAT -> {
                    Toast.makeText(requireContext(), "Contato selecionado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val chat = listOf(
            Chat(1, "Elibe"),
            Chat(2, "Tonin"),
            Chat(3, "Bill"),
            Chat(4, "Thegas"),
            Chat(5, "Dudi"),
        )

        adapter.submitList(chat)

        initListener()
    }

    private fun initListener() {
        binding.btnChat.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}