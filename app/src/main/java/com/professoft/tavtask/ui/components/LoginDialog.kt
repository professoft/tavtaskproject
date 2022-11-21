package com.professoft.tavtask.ui.components

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.ViewGroup
import android.widget.EditText
import com.professoft.tavtask.R
import com.professoft.tavtask.databinding.DialogLoginBinding
import com.professoft.tavtask.utils.UtilityClass

class LoginDialog(context: Context, var loginClickListener: (mail: String, password: String) -> Unit)
: Dialog(context, R.style.Theme_TavTaskProject) {

    private lateinit var binding: DialogLoginBinding
    private lateinit var mailEditText: EditText
    private lateinit var passwordEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        mailEditText = binding.mailEditText
        passwordEditText = binding.passwordEditText
        binding.loginButton.setOnClickListener {
            if(nullCheck(mailEditText.text,passwordEditText.text)) {
                loginClickListener(
                    mailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
    }

    private fun nullCheck(mail: Editable, password: Editable): Boolean{

        if(mail.isNullOrEmpty()  || !UtilityClass.isEmailValid(mail.toString())){
           mailEditText.error = context.getString(R.string.enterValidEmail)
        }
        if(password.isNullOrEmpty()){
            passwordEditText.error = context.getString(R.string.enterPassword)
        }

        return  mailEditText.error.isNullOrEmpty() &&
                passwordEditText.error.isNullOrEmpty()
    }

}