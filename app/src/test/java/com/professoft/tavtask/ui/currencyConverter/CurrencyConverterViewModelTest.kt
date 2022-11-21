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
    private val formattedBigDecimal: String = "0.15"
    private val resultOfCurrenyConverter: String = "0.10"
    private var currencyConverterViewModel: CurrencyConverterViewModel = CurrencyConverterViewModel()

    @Test
    fun testBigDecimalFormatter() {
        var result = currencyConverterViewModel.bigDecimalFormat(bigDecimalInput).toString()
        Assert.assertEquals(result,formattedBigDecimal)
    }
    @Test
    fun testCurrencyConverter() {
        var result = currencyConverterViewModel.convertCurrency(bigDecimalInput,bigDecimalRatio).toString()
        Assert.assertEquals(result,resultOfCurrenyConverter)
    }
}