package com.mylb.mylogbook.presentation

import android.content.Context
import android.content.Intent

object SystemIntent {

    fun openMail(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        context.startActivity(intent)
    }
}
