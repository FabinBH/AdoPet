package com.example.projetopi.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projetopi.R
import com.example.projetopi.data.model.Pet
import com.example.projetopi.databinding.ItemAdopetBinding

class PetAdapter (
    private val context: Context,
    private val petSelected: (Pet, Int) -> Unit
): ListAdapter<Pet, PetAdapter.MyViewHolder>(DIFF_CALBACK) {

    companion object {
        const val SELECT_ADOPT = 1
        private val DIFF_CALBACK = object : DiffUtil.ItemCallback<Pet>() {
            override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemAdopetBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pet = getItem(position)
        holder.bind(pet)
    }

    inner class MyViewHolder(val binding: ItemAdopetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(pet: Pet) {
            binding.txtNome.text = "Nome: ${pet.nome}"
            binding.txtInfo1.text = "Espécie: ${pet.especie}"
            binding.txtInfo2.text = "Raça: ${pet.raca}"
            binding.txtInfo3.text = "Idade: ${pet.idade} meses"
            binding.txtInfo4.text = "Breve descrição do animal: ${pet.descricao}"

            if (!pet.fotoUrl.isNullOrEmpty()) {
                try {
                    var base64 = pet.fotoUrl

                    if (base64.startsWith("data:image")) {
                        base64 = base64.substring(base64.indexOf(",") + 1)
                    }

                    val imageBytes = Base64.decode(base64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                    binding.imgPet.setImageBitmap(bitmap)

                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.imgPet.setImageResource(R.drawable.ic_animal)
                }
            } else {
                binding.imgPet.setImageResource(R.drawable.ic_animal)
            }

            binding.root.setOnClickListener {
                petSelected(pet, SELECT_ADOPT)
            }
        }
    }
}