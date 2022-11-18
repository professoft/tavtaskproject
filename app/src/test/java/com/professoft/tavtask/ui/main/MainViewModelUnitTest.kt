package com.professoft.tavtask.ui.main

import com.professoft.tavtask.data.realm.RealmDatabase
import com.professoft.tavtask.utils.UtilityClass
import io.realm.kotlin.RealmConfiguration
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.powermock.modules.junit4.PowerMockRunner


/**
 * A Class covering MainViewModel login test cases
 *
 * Test Index:
 *
 * 1. When email is invalid, show invalid email error
 *
 * 2. When email and password is valid, do login
 *
 */

@RunWith(PowerMockRunner::class)
class MainViewModelUnitTest {
    companion object {
        private const val VerifiedEmail = "user@test.com"
        private const val UnVerifiedEmail = "test.com"
        private const val PASSWORD = "1234"


        private val realmDatabase : RealmDatabase = mock(RealmDatabase::class.java)
        private var loginViewModel =  MainViewModel(mock(RealmDatabase::class.java))
 
        @Test
        fun writeDefaultUser() {
            verify(realmDatabase.writeRealm(VerifiedEmail, PASSWORD, false))
        }

        @Test
        fun testIfEmailInvalid_ReportEmailError() {
           verify(loginViewModel.checkUser(UnVerifiedEmail, PASSWORD))

        }

        @Test
        fun testIfEmailValid_DoLogin() {
            verify(loginViewModel.checkUser(VerifiedEmail, PASSWORD))
        }
    }
}