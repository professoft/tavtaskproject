package com.professoft.tavtask.utils

import retrofit2.Response

sealed class LoginDataState {
    data class Error(val message: String?) : LoginDataState()
    object ValidCredentialsState : LoginDataState()
    object InValidEmailState : LoginDataState()
    class Success(val body: Response<UserModel>? = null) : LoginDataState()
}