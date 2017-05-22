package com.mylb.mylogbook.presentation.ui.activity

import android.graphics.Color
import android.os.Bundle
import com.mylb.mylogbook.presentation.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        window.statusBarColor = Color.TRANSPARENT

        signUpButton.setOnClickListener { this.navigator.navigateToSignUp(this) }

        logInButton.setOnClickListener { this.navigator.navigateToLogIn(this) }
    }
}