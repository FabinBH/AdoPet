package com.example.projetopi.ui.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PhoneMask(private val editText: EditText) : TextWatcher {

    private var isUpdating = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isUpdating) return

        val digits = s.toString().replace("[^0-9]".toRegex(), "")
        val limited = digits.take(11)

        val formatted = when {
            limited.length <= 2 -> {
                "(${limited}"
            }
            limited.length <= 6 -> {
                "(${limited.substring(0, 2)}) ${limited.substring(2)}"
            }
            limited.length <= 10 -> {
                "(${limited.substring(0, 2)}) ${limited.substring(2, 6)}-${limited.substring(6)}"
            }
            else -> {
                "(${limited.substring(0, 2)}) ${limited.substring(2, 7)}-${limited.substring(7)}"
            }
        }

        isUpdating = true
        editText.setText(formatted)
        editText.setSelection(formatted.length)
        isUpdating = false
    }

    override fun afterTextChanged(s: Editable?) {}
}
