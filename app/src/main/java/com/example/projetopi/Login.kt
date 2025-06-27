package com.example.projetopi

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import com.example.projetopi.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var senhaVisivel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mostrarOcultarSenha()
        mostrarCriarConta()
        mostrarDashboard()
        voltar()
    }

    private fun mostrarOcultarSenha() {
        binding.ivToggleSenha.setOnClickListener {
            senhaVisivel = !senhaVisivel

            if (senhaVisivel) {
                binding.etSenha.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.ivToggleSenha.setImageResource(R.drawable.olhomostrar)
            } else {
                binding.etSenha.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.ivToggleSenha.setImageResource(R.drawable.olhoocultar)
            }
        }
    }

    private fun voltar() {
        binding.imageButton.setOnClickListener {
            finish()
        }
    }

    private fun mostrarCriarConta() {
        binding.textView9.setOnClickListener {
            val intent = Intent(this, CriarConta::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun mostrarDashboard() {
        binding.button.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
    }
}