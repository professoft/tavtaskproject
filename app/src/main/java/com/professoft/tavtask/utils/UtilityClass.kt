package com.professoft.tavtask.utils

import java.util.regex.Pattern
object UtilityClass {
    private val EMAIL = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun isEmailValid(email: String): Boolean {
        return if (email.isEmpty()) {
            false
        } else {
            EMAIL.matcher(email).matches()
        }
    }
}