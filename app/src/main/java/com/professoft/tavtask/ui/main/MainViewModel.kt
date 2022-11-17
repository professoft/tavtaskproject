package com.professoft.tavtask.ui.main

import androidx.lifecycle.viewModelScope
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.data.model.User
import com.professoft.tavtask.data.realm.RealmDatabase
import com.professoft.tavtask.ui.components.LoginDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val realmDatabase: RealmDatabase) :
    BaseViewModel(), LoginDialog.Listener {
    private val mutableViewState4Login = MutableStateFlow<UserLoginSealed>(UserLoginSealed.IDLE)
    private val mutableViewState4ActiveUser = MutableStateFlow<UserActiveSealed>(UserActiveSealed.IDLE)
    val viewState4Login: StateFlow<UserLoginSealed> = mutableViewState4Login
    val viewState4ActiveUser: StateFlow<UserActiveSealed> = mutableViewState4ActiveUser
    private lateinit var realmResults: RealmResults<User>


    fun checkDefaultUser() {
        viewModelScope.launch {
            runCatching {
                realmResults = realmDatabase.getUser()
                mutableViewState4Login.value = UserLoginSealed.LOADING

            }.onFailure {
                mutableViewState4Login.value = UserLoginSealed.ERROR

            }.onSuccess {
                if (realmResults.size > 0) {
                    mutableViewState4Login.value = UserLoginSealed.SUCCESS
                } else {
                    mutableViewState4Login.value = UserLoginSealed.ERROR
                }
            }
        }
    }

    fun checkActiveUser() {
        viewModelScope.launch {
            runCatching {
                realmResults = realmDatabase.findActiveUser()
                mutableViewState4ActiveUser.value = UserActiveSealed.LOADING

            }.onFailure {
                mutableViewState4ActiveUser.value = UserActiveSealed.ERROR

            }.onSuccess {
                if (realmResults.size > 0) {
                    mutableViewState4ActiveUser.value = UserActiveSealed.SUCCESS
                } else {
                    mutableViewState4ActiveUser.value = UserActiveSealed.ERROR
                }
            }
        }
    }



    override fun loginClicked(mail: String,password: String) {
        viewModelScope.launch {
            runCatching {
                mutableViewState4Login.value = UserLoginSealed.LOADING
                realmResults = realmDatabase.checkUser(mail,password)

            }.onFailure {
                mutableViewState4Login.value = UserLoginSealed.ERROR

            }.onSuccess {
                if (realmResults.size > 0) {
                    lateinit var userId : ObjectId
                    realmResults.forEach{
                      userId  = it._id
                   }
                    realmDatabase.logInUser(mail)
                    mutableViewState4Login.value = UserLoginSealed.SUCCESS
                } else {
                    mutableViewState4Login.value = UserLoginSealed.ERROR
                }
            }
        }
    }

    fun registrationDefaultUser(mail: String,password: String) {
        viewModelScope.launch {
            runCatching {
                mutableViewState4Login.value = UserLoginSealed.LOADING

            }.onFailure {
                mutableViewState4Login.value = UserLoginSealed.ERROR

            }.onSuccess {
                realmDatabase.writeRealm(mail, password,false)
                mutableViewState4Login.value = UserLoginSealed.SUCCESS
            }
        }
    }
    sealed class UserLoginSealed {
        object IDLE : UserLoginSealed()
        object LOADING : UserLoginSealed()
        object ERROR : UserLoginSealed()
        object SUCCESS : UserLoginSealed()
    }
    sealed class UserActiveSealed {
        object IDLE : UserActiveSealed()
        object LOADING : UserActiveSealed()
        object ERROR : UserActiveSealed()
        object SUCCESS : UserActiveSealed()
    }
}