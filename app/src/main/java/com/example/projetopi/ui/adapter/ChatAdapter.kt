package com.example.projetopi.ui.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.data.model.Chat
import com.example.projetopi.databinding.ItemChatBinding

class ChatAdapter(
    private val context: Context,
    private val chatListener: (Chat, Int) -> Unit
) : ListAdapter<Chat, ChatAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        const val SELECT_CHAT = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.id == newItem.id && oldItem.nome == newItem.nome
            }

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

    inner class MyViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            binding.profileName.text = chat.nome
            binding.lastMessage.text = chat.email

            if (!chat.fotoBase64.isNullOrEmpty()) {
                try {
                    val bytes = Base64.decode(chat.fotoBase64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    binding.profilePhoto.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.profilePhoto.setImageResource(android.R.drawable.ic_menu_report_image)
                }
            } else {
                binding.profilePhoto.setImageResource(android.R.drawable.ic_menu_report_image)
            }

            binding.root.setOnClickListener {
                chatListener(chat, SELECT_CHAT)
            }
        }
    }
}
