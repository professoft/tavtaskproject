package com.professoft.tavtask.main

import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.professoft.tavtask.data.datastore.DataStoreManager
import com.professoft.tavtask.data.realm.RealmDatabase
import com.professoft.tavtask.ui.main.MainViewModel
import io.realm.kotlin.internal.platform.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


const val TEST_EMAIL = "user@test.com"
const val TEST_PASSWORD = "1234"

@RunWith(JUnit4::class)
@SmallTest
class MainViewModelTest {

    private var realmDatabase: RealmDatabase? = null
    private var dataStoreManager: DataStoreManager? = null
    private var mainViewModel: MainViewModel? = null


    @Before
    fun create() {
        realmDatabase = RealmDatabase()
        dataStoreManager = DataStoreManager(InstrumentationRegistry.getInstrumentation().context)
        mainViewModel = MainViewModel(dataStoreManager!!,realmDatabase!!)
    }

    @Test
    fun testRegistrationDefaultUser() {
        mainViewModel!!.registrationDefaultUser(TEST_EMAIL,TEST_PASSWORD)
        val result = realmDatabase!!.checkUser(TEST_EMAIL,TEST_PASSWORD)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun testCheckUser() = runBlocking {
        realmDatabase!!.writeRealm(TEST_EMAIL,TEST_PASSWORD, false)
        mainViewModel!!.checkUser(TEST_EMAIL,TEST_PASSWORD)
        val result = mainViewModel!!.login.value
        assertThat(result).isEqualTo(true)
    }


    @Test
    fun checkActiveUser() {
            realmDatabase!!.writeRealm(TEST_EMAIL,TEST_PASSWORD, true)
            mainViewModel!!.checkActiveUser()
            val result = mainViewModel!!.login.value
        assertThat(result).isEqualTo(true)
    }
}