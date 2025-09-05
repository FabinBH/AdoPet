package com.example.projetopi.ui.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projetopi.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    /*private lateinit var checkBoxCaes: CheckBox
    private lateinit var checkBoxGatos: CheckBox
    private lateinit var checkBoxPassaros: CheckBox
    private lateinit var checkBoxRoedores: CheckBox

    // CheckBoxes Idade
    private lateinit var checkBoxMenor6Meses: CheckBox
    private lateinit var checkBoxEntre6MesesE2Anos: CheckBox
    private lateinit var checkBoxEntre2E5Anos: CheckBox
    private lateinit var checkBoxMaior5Anos: CheckBox

    private lateinit var buttonAplicarFiltros: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializando CheckBoxes de Animal
        checkBoxCaes = findViewById(R.id.checkBox)
        checkBoxGatos = findViewById(R.id.checkBox2)
        checkBoxPassaros = findViewById(R.id.checkBox3)
        checkBoxRoedores = findViewById(R.id.checkBox4)

        // Inicializando CheckBoxes de Idade
        checkBoxMenor6Meses = findViewById(R.id.checkBox5)
        checkBoxEntre6MesesE2Anos = findViewById(R.id.checkBox6)
        checkBoxEntre2E5Anos = findViewById(R.id.checkBox7)
        checkBoxMaior5Anos = findViewById(R.id.checkBox8)

        // Botão aplicar filtros
        buttonAplicarFiltros = findViewById(R.id.button)

        buttonAplicarFiltros.setOnClickListener {
            aplicarFiltros()
        }
    }

    private fun aplicarFiltros() {
        val animaisSelecionados = mutableListOf<String>()
        val idadesSelecionadas = mutableListOf<String>()

        if (checkBoxCaes.isChecked) animaisSelecionados.add("Cães")
        if (checkBoxGatos.isChecked) animaisSelecionados.add("Gatos")
        if (checkBoxPassaros.isChecked) animaisSelecionados.add("Pássaros")
        if (checkBoxRoedores.isChecked) animaisSelecionados.add("Roedores")

        if (checkBoxMenor6Meses.isChecked) idadesSelecionadas.add("Menor que 6 meses")
        if (checkBoxEntre6MesesE2Anos.isChecked) idadesSelecionadas.add("Entre 6 meses e 2 ano(s)")
        if (checkBoxEntre2E5Anos.isChecked) idadesSelecionadas.add("Entre 2 e 5 ano(s)")
        if (checkBoxMaior5Anos.isChecked) idadesSelecionadas.add("Maior que 5 ano(s)")

        val mensagem = StringBuilder()
        mensagem.append("Animais selecionados: ${animaisSelecionados.joinToString(", ")}\n")
        mensagem.append("Idades selecionadas: ${idadesSelecionadas.joinToString(", ")}")

        Toast.makeText(this, mensagem.toString(), Toast.LENGTH_LONG).show()
    }*/
}