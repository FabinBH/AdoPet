package com.example.projetopi.ui.adapter

import android.R.drawable
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.data.model.Ong
import com.example.projetopi.databinding.ItemOngBinding

class OngAdapter(
    private val context: Context,
    private val ongSelected: (Ong, Int) -> Unit
) : ListAdapter<Ong, OngAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        const val SELECT_ONG = 1

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
        val binding = ItemOngBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ong = getItem(position)
        holder.bind(ong)
    }

    inner class MyViewHolder(val binding: ItemOngBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ong: Ong) {
            binding.txtOngName.text = ong.nome
            binding.txtInfo1.text = ong.cnpj
            binding.txtInfo2.text = ong.email
            binding.txtInfo3.text = ong.telefone

            if (!ong.fotoUrl.isNullOrEmpty()) {
                try {
                    var base64 = ong.fotoUrl

                    if (base64.startsWith("data:image")) {
                        base64 = base64.substring(base64.indexOf(",") + 1)
                    }

                    val imageBytes = Base64.decode(base64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                    binding.imgOng.setImageBitmap(bitmap)

                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.imgOng.setImageResource(drawable.ic_menu_report_image)
                }
            } else {
                binding.imgOng.setImageResource(drawable.ic_menu_report_image)
            }

            binding.root.setOnClickListener {
                ongSelected(ong, SELECT_ONG)
            }
        }
    }
}