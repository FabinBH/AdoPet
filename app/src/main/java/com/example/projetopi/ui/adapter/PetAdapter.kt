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
import com.example.projetopi.databinding.ItemAdopetBinding

class PetAdapter(
    private val context: Context,
    private val taskSelected: (Pet, Int) -> Unit
): ListAdapter<Pet, PetAdapter.MyViewHolder>(DIFF_CALBACK) {

    companion object {
        private val DIFF_CALBACK = object : DiffUtil.ItemCallback<Pet>() {
            override fun areItemsTheSame(
                oldItem: Pet, newItem: Pet
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.description == newItem.description
            }

            override fun areContentsTheSame(
                oldItem: Pet, newItem: Pet
            ): Boolean {
                return oldItem == newItem && oldItem.description == newItem.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemAdopetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = getItem(position)
        holder.binding.textDescription.text = task.description

        setIndicators(task, holder)
    }

    private fun setIndicators(task: Task, holder: com.fabio.task.ui.adapter.TaskAdapter.MyViewHolder){
        when(task.status){
            Status.TODO -> {
                holder.binding.buttonBack.isVisible = false
                holder.binding.buttonFoward.setOnClickListener { taskSelected(task, SELECT_NEXT) }
            }
            Status.DOING -> {
                holder.binding.buttonBack.setColorFilter(ContextCompat.getColor(context, R.color.color_status_todo))
                holder.binding.buttonFoward.setColorFilter( ContextCompat.getColor(context, R.color.color_status_done))
                holder.binding.buttonFoward.setOnClickListener { taskSelected(task, SELECT_NEXT) }
                holder.binding.buttonBack.setOnClickListener { taskSelected(task, SELECT_BACK) }


            }
            Status.DONE -> {
                holder.binding.buttonFoward.isVisible = false
                holder.binding.buttonBack.setOnClickListener { taskSelected(task, SELECT_BACK) }

            }
        }
        holder.binding.buttonDelete.setOnClickListener { taskSelected(task, SELECT_REMOVER)}
        holder.binding.buttonEditar.setOnClickListener { taskSelected(task, SELECT_EDIT) }
        holder.binding.buttonDetails.setOnClickListener { taskSelected(task, SELECT_DETAILS) }
    }

    inner class MyViewHolder(val binding: ItemAdopetBinding): RecyclerView.ViewHolder(binding.root) {

    }
}