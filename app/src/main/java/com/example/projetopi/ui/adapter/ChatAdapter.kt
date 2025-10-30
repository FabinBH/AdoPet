package com.example.projetopi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.data.model.Chat
import com.example.projetopi.databinding.ItemChatBinding

class ChatAdapter (
    private val context: Context,
    private val chatTitle: (Chat, Int) -> Unit
): ListAdapter<Chat, ChatAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val SELECT_CHAT = 1
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.id == newItem.id && oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem == newItem && oldItem.name == newItem.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    inner class MyViewHolder(val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root) {

    }
}