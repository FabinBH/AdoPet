package com.example.projetopi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentSocialBinding

class SocialFragment : Fragment() {

    private var _binding: FragmentSocialBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSocialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val likeButton = view.findViewById<ImageView>(R.id.iv_like)
        val bookmarkButton = view.findViewById<ImageView>(R.id.iv_bookmark)

        likeButton?.setOnClickListener {
            it.isSelected = !it.isSelected
        }

        bookmarkButton?.setOnClickListener {
            it.isSelected = !it.isSelected
        }

        initListeners()
    }

    private fun initListeners() {
        binding.btnChat.setOnClickListener {
            findNavController().navigate(R.id.chatFragment)
        }
    }
}