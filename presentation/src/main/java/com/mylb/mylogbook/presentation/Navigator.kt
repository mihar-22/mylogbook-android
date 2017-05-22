package com.mylb.mylogbook.presentation

import android.content.Context
import android.content.Intent
import com.mylb.mylogbook.presentation.ui.activity.auth.LogInActivity
import com.mylb.mylogbook.presentation.ui.activity.auth.SignUpActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {

    fun navigateToSignUp(context: Context) {
        val intent = SignUpActivity.getCallingIntent(context)

        context.startActivity(intent)
    }

    fun navigateToLogIn(context: Context) {
        val intent = LogInActivity.getCallingIntent(context)

        context.startActivity(intent)
    }

    fun navigateToDefaultMail(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)

        intent.addCategory(Intent.CATEGORY_APP_EMAIL)

        context.startActivity(intent)
    }
}
