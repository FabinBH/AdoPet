package com.example.projetopi.ui.util

class CepMask(private val editText: android.widget.EditText) : android.text.TextWatcher {

    private var isUpdating = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isUpdating) return

        val digits = s.toString().replace("[^0-9]".toRegex(), "")
        val limited = digits.take(8)

        val formatted = when {
            limited.length <= 5 -> limited
            else -> limited.substring(0, 5) + "-" + limited.substring(5)
        }

        isUpdating = true
        editText.setText(formatted)
        editText.setSelection(formatted.length)
        isUpdating = false
    }

    override fun afterTextChanged(s: android.text.Editable?) {}
}