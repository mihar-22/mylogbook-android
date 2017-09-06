package com.mylb.mylogbook.presentation

import android.content.Context
import android.content.Intent

object SystemIntent {

    fun openMail(context: Context, email: String? = null, subject: String? = null, text: String? = null) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(intent)
    }

}
