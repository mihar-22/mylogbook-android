package com.mylb.mylogbook.presentation.util

import android.view.MenuItem

fun MenuItem.enable(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    this.icon.alpha = if (isEnabled) 255 else 100
}
