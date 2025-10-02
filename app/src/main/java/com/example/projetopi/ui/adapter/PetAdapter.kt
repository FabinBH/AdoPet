package com.example.projetopi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.data.model.Pet
import com.example.projetopi.data.model.Status
import com.example.projetopi.databinding.ItemAdopetBinding

class PetAdapter(
    private val context: Context,
    private val taskSelected: (Pet, Int) -> Unit
): ListAdapter<Pet, PetAdapter.MyViewHolder>(DIFF_CALBACK) {

    companion object {
        val SELECT_DETAILS = 1
        val SELECT_ADOPT = 2
        private val DIFF_CALBACK = object : DiffUtil.ItemCallback<Pet>() {
            override fun areItemsTheSame(
                oldItem: Pet, newItem: Pet
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.adopted == newItem.adopted
            }

            override fun areContentsTheSame(
                oldItem: Pet, newItem: Pet
            ): Boolean {
                return oldItem == newItem && oldItem.adopted == newItem.adopted
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemAdopetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pet = getItem(position)
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