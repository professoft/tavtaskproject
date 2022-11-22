package com.professoft.tavtask.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.data.datastore.DataStoreRepo
import com.professoft.tavtask.data.model.User
import com.professoft.tavtask.data.realm.RealmDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val datastoreRepo: DataStoreRepo, private var realmDatabase: RealmDatabase
) : BaseViewModel() {

    val checkLogin: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var realmResults: RealmResults<User>
    private var checkResults = false
    val checkActiveUser: MutableLiveData<Boolean> = MutableLiveData()
    val checkRegistration: MutableLiveData<Boolean> = MutableLiveData()


    fun checkDefaultUser() {
        viewModelScope.launch {
            runCatching {
                realmResults = realmDatabase.getUser()
            }.onFailure {

                checkRegistration.postValue(false)

            }.onSuccess {
                if (realmResults.size > 0) {
                    checkRegistration.postValue(true)
                } else {
                    checkRegistration.postValue(false)
                }
            }
        }
    }




    fun checkUser(mail: String, password: String)  {
        viewModelScope.launch {
            runCatching {
                checkResults = realmDatabase.checkUser(mail, password)

            }.onFailure {

                checkLogin.postValue(false)

            }.onSuccess {
                if (checkResults) {
                    login.value = true
                    realmDatabase.logInUser(mail)
                    checkLogin.postValue(true)
                } else {
                    checkLogin.postValue(false)
                }
            }
        }
    }

    fun registrationDefaultUser(mail: String, password: String) {
        viewModelScope.launch {
            runCatching {
            }.onFailure {
                checkLogin.postValue(false)

            }.onSuccess {
                realmDatabase.writeRealm(mail, password, false)
                checkLogin.postValue(true)
            }
        }
    }

    fun checkActiveUser() {
        viewModelScope.launch {
            runCatching {
                realmResults = realmDatabase.findActiveUser()

            }.onFailure {
                checkActiveUser.postValue(false)

            }.onSuccess {
                storeDefaultOffset()
                if (realmResults.size > 0) {
                    login.value = true
                    checkActiveUser.postValue(true)
                } else {
                    checkActiveUser.postValue(false)
                }
            }
        }
    }
    private fun storeDefaultOffset() = runBlocking {
        datastoreRepo.putString("offset", "0")
    }

}


