package com.mylb.mylogbook.presentation.ui.activity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity

class LogInActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, LogInActivity::class.java)
        }
    }
}
