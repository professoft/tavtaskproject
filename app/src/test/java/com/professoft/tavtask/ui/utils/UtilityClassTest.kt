package com.professoft.tavtask.ui.utils

import com.professoft.tavtask.utils.UtilityClass
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.random.Random

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
        var randomPosition = Random.nextInt(6)
        var result = UtilityClass.isEmailValid(unVerifiedEmailAdresses[randomPosition])
        Assert.assertEquals(result,true)
    }

    @Test
    fun testValidEmail() {
        var result = UtilityClass.isEmailValid(verifiedEmail)
        Assert.assertEquals(result,true)
    }

}