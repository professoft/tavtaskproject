package com.professoft.tavtask.ui.currencyConverter

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal

@RunWith(JUnit4::class)

internal class CurrencyConverterViewModelTest{

    private val bigDecimalInput: BigDecimal = BigDecimal(0.20000)
    private val bigDecimalRatio: BigDecimal = BigDecimal(0.50000)
    private val resultOfCurrencyConverter: String = "0.10"
    private var currencyConverterViewModel: CurrencyConverterViewModel = CurrencyConverterViewModel()

    @Test
    fun testCurrencyConverter() {
        var result = currencyConverterViewModel.convertCurrency(bigDecimalInput,bigDecimalRatio).toString()
        Assert.assertEquals(result,resultOfCurrencyConverter)
    }
}