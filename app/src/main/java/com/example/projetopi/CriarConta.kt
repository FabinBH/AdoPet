package com.example.projetopi

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.projetopi.databinding.ActivityCriarcontaBinding

class CriarConta : AppCompatActivity() {
    private lateinit var binding: ActivityCriarcontaBinding
    private var senhaVisivel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriarcontaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mostrarOcultarSenha()
        verificarEmail()
        mostrarLogin()
        mostrarDashboard()
        voltar()
    }

    private fun mostrarOcultarSenha() {
        binding.ivToggleSenha.setOnClickListener {
            senhaVisivel = !senhaVisivel

            if (senhaVisivel) {
                binding.etSenha.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.ivToggleSenha.setImageResource(R.drawable.olhomostrar)
            } else {
                binding.etSenha.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.ivToggleSenha.setImageResource(R.drawable.olhoocultar)
            }
        }
    }

    private fun verificarEmail() {
        binding.Email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                val emailValido = email.contains("@gmail.com") || email.contains("@hotmail.com")

                if (emailValido) {
                    binding.imageView2.visibility = android.view.View.VISIBLE
                } else {
                    binding.imageView2.visibility = android.view.View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun voltar() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun mostrarLogin() {
        binding.textView9.setOnClickListener {
            val intent = Intent(this, Login::class.java)
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
