package com.professoft.tavtask.ui.components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.professoft.tavtask.R
import com.professoft.tavtask.databinding.DialogLoginBinding
import java.util.regex.Pattern

class LoginDialog : DialogFragment() {

    var listener: Listener? = null
    private lateinit var binding: DialogLoginBinding
    lateinit var loginDialog : Dialog
    private lateinit var mailEditText: EditText
    private lateinit var passwordEditText: EditText
    private val emailAddressPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoginBinding.inflate(LayoutInflater.from(context))
        mailEditText = binding.mailEditText
        passwordEditText = binding.passwordEditText
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding.loginButton.setOnClickListener {
            if(nullCheck()) {
                listener?.loginClicked(
                    mailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }

        loginDialog = AlertDialog.Builder(requireActivity()).setView(binding.root).create()
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    private fun nullCheck() : Boolean{

        if(mailEditText.text.isNullOrEmpty()  || !isValidMail(mailEditText.text.toString())){
           mailEditText.error = getString(R.string.enterValidEmail)
        }
        if(passwordEditText.text.isNullOrEmpty()){
            passwordEditText.error = getString(R.string.enterPassword)
        }

        return  mailEditText.error.isNullOrEmpty() &&
                passwordEditText.error.isNullOrEmpty()
    }

    interface Listener {
        fun loginClicked(mail : String, password : String)
    }

    private fun isValidMail(mail: String): Boolean {
        return emailAddressPattern.matcher(mail).matches()
    }

    fun checkAndDismiss() {
        if (loginDialog.isShowing) {
            dismiss()
        }
    }
}