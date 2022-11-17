package com.professoft.tavtaskproject.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.professoft.tavtaskproject.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    private var baseViewModel: BaseViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
    }

    abstract fun initViewBinding()


}