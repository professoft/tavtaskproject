package com.professoft.tavtask.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.professoft.tavtask.base.BaseViewModel
import com.professoft.tavtask.data.datastore.DatastoreRepo
import com.professoft.tavtask.data.model.User
import com.professoft.tavtask.data.realm.RealmDatabase
import com.professoft.tavtask.repositories.FlightRepository
import com.professoft.tavtask.utils.FlightsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val realmDatabase: RealmDatabase,
                                        private var datastoreRepo: DatastoreRepo)
    : BaseViewModel() {

    val checkLogin : MutableLiveData<Boolean> = MutableLiveData()
    val checkActiveUser : MutableLiveData<Boolean> = MutableLiveData()
    val checkRegistration : MutableLiveData<Boolean> = MutableLiveData()
    private var mRepository = FlightRepository.getInstance()
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()
    private var airport: MutableLiveData<List<FlightsResponse>> =
        MutableLiveData<List<FlightsResponse>>().apply { value = emptyList() }
    private lateinit var realmResults: RealmResults<User>


    fun checkDefaultUser() {
        viewModelScope.launch {
            runCatching {
                realmResults = realmDatabase.getUser()
                loading.value = true
            }.onFailure {
                loading.value = false

                checkRegistration.postValue(false)

            }.onSuccess {
                loading.value = false

                if (realmResults.size > 0) {
                    checkRegistration.postValue(true)
                } else {
                    checkRegistration.postValue(false)
                }
            }
        }
    }

    fun checkActiveUser() {
        viewModelScope.launch {
            runCatching {
                loading.value = true
                realmResults = realmDatabase.findActiveUser()

            }.onFailure {
                loading.value = false
                checkActiveUser.postValue(false)

            }.onSuccess {
                loading.value = false

                if (realmResults.size > 0) {
                    checkActiveUser.postValue(true)
                } else {
                    checkActiveUser.postValue(false)
                }
            }
        }
    }



    fun checkUser(mail: String, password: String) {
        viewModelScope.launch {
            runCatching {
                loading.value = true
                realmResults = realmDatabase.checkUser(mail,password)

            }.onFailure {
                loading.value = false

                checkLogin.postValue(false)

            }.onSuccess {
                loading.value = false

                if (realmResults.size > 0) {
                    lateinit var userId : ObjectId
                    realmResults.forEach{
                      userId  = it._id
                   }
                    realmDatabase.logInUser(mail)
                    checkLogin.postValue(true)
                } else {
                    checkLogin.postValue(false)
                }
            }
        }
    }

    fun registrationDefaultUser(mail: String,password: String) {
        viewModelScope.launch {
            runCatching {
                loading.value = true
            }.onFailure {
                loading.value = false
                checkLogin.postValue(false)

            }.onSuccess {
                loading.value = false
                realmDatabase.writeRealm(mail, password,false)
                checkLogin.postValue(true)
            }
        }
    }

    }


