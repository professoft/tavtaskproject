package com.professoft.tavtask.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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

@RunWith(JUnit4::class)
internal class UtilityClassTest{

        private val verifiedEmail = "user@test.com"
        private val unVerifiedEmailAdresses = listOf("test.com", "u@test.com" , "@test.com", "user@t.com" , "user@test.c", "", "usertestcom")

    @Test
    fun testInvalidEmail() {
        for (email in unVerifiedEmailAdresses) {
            var result = UtilityClass.isEmailValid(email)
            assertThat(result).isEqualTo(true)
        }
    }

    @Test
    fun testValidEmail() {
        var result = UtilityClass.isEmailValid(verifiedEmail)
        assertThat(result).isEqualTo(true)
    }

}