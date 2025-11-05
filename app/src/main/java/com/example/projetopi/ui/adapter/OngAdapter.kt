package com.example.projetopi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.data.model.Ong
import com.example.projetopi.databinding.ItemAdopetBinding

class OngAdapter (
    private val context: Context,
    private val taskSelected: (Ong, Int) -> Unit
): ListAdapter<Ong, OngAdapter.MyViewHolder>(DIFF_CALBACK) {

    companion object {
        val SELECT_DETAILS = 1
        val SELECT_ADOPT = 2
        private val DIFF_CALBACK = object : DiffUtil.ItemCallback<Ong>() {
            override fun areItemsTheSame(
                oldItem: Ong, newItem: Ong
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.nome == newItem.nome
            }

            override fun areContentsTheSame(
                oldItem: Ong, newItem: Ong
            ): Boolean {
                return oldItem == newItem && oldItem.nome == newItem.nome
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemAdopetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ong = getItem(position)
        //holder.binding.textDescription.text = pet.description

        //setIndicators(pet, holder)
    }

    /*private fun setIndicators(pet: Pet, holder: MyViewHolder){
        when(pet.adopted){
            Status.NOT_ADOPTED -> {
                holder.binding.btnDetalhes.isVisible = false
                holder.binding.btnDetalhes.setOnClickListener { taskSelected(task, SELECT_NEXT) }
            }

        }
        holder.binding.buttonDelete.setOnClickListener { taskSelected(task, SELECT_REMOVER)}
        holder.binding.buttonEditar.setOnClickListener { taskSelected(task, SELECT_EDIT) }
        holder.binding.buttonDetails.setOnClickListener { taskSelected(task, SELECT_DETAILS) }
    }*/

    inner class MyViewHolder(val binding: ItemAdopetBinding): RecyclerView.ViewHolder(binding.root) {

    }
}