package com.akshar.one.view.login

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.akshar.one.R

class OTPTextWatcher(val view: View, private val editTextList: List<AppCompatEditText>): TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val string = s.toString()
        when(view.id){
            R.id.edt1 -> { if(string.length == 1) editTextList[1].requestFocus()}
            R.id.edt2 -> { if(string.length == 1) editTextList[2].requestFocus() else editTextList[0].requestFocus()}
            R.id.edt3 -> { if(string.length == 1) editTextList[3].requestFocus() else editTextList[1].requestFocus()}
            R.id.edt4 -> { if(string.length == 1) editTextList[4].requestFocus() else editTextList[2].requestFocus()}
            R.id.edt5 -> { if(string.length == 1) editTextList[5].requestFocus() else editTextList[3].requestFocus()}
            R.id.edt6 -> { if(string.isEmpty()) editTextList[4].requestFocus()}
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}