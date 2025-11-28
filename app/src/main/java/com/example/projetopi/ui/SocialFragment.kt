package com.example.projetopi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.projetopi.R

class SocialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_social, container, false)
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}