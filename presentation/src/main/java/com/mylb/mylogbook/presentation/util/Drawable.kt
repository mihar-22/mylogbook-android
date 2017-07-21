package com.mylb.mylogbook.presentation.util

import android.content.Context
import android.graphics.drawable.Drawable
import com.mylb.mylogbook.domain.resource.Car

fun Context.getDrawable(name: String): Drawable {
    val resId = this.resources.getIdentifier(name, "drawable", this.packageName)

    return this.resources.getDrawable(resId)
}

fun Context.carDrawable(type: String) = this.getDrawable("car_${type.replace(" ", "_")}")
