package com.example.projetopi.ui.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.data.model.Ong
import com.example.projetopi.databinding.ItemAdopetBinding

class OngAdapter(
    private val context: Context,
    private val ongSelected: (Ong, Int) -> Unit
) : ListAdapter<Ong, OngAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        const val SELECT_DETAILS = 1
        const val SELECT_ADOPT = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Ong>() {
            override fun areItemsTheSame(oldItem: Ong, newItem: Ong): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Ong, newItem: Ong): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAdopetBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ong = getItem(position)
        holder.bind(ong)
    }

    inner class MyViewHolder(val binding: ItemAdopetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ong: Ong) {
            binding.txtOngName.text = ong.nome

            if (ong.fotoUrl.isNotEmpty()) {
                try {
                    val imageBytes = Base64.decode(ong.fotoUrl, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    binding.imgOng.setImageBitmap(decodedImage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            binding.btnAdotar.setOnClickListener { ongSelected(ong, SELECT_ADOPT) }
            binding.btnDetalhes.setOnClickListener { ongSelected(ong, SELECT_DETAILS) }
        }
    }
}
