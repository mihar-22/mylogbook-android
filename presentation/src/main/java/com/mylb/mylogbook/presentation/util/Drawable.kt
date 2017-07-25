package com.mylb.mylogbook.presentation.util

import android.content.Context
import android.graphics.drawable.Drawable
import com.mylb.mylogbook.domain.resource.car.CarBody
import com.mylb.mylogbook.domain.resource.supervisor.SupervisorGender

fun Context.getDrawable(name: String): Drawable {
    val resId = this.resources.getIdentifier(name, "drawable", this.packageName)

    return this.resources.getDrawable(resId)
}

fun Context.carDrawable(body: CarBody) = this.getDrawable("car_${body.type.replace(" ", "_")}")

fun Context.supervisorDrawable(gender: SupervisorGender, isAccredited: Boolean) =
        this.getDrawable("supervisor_${gender.displayName.toLowerCase()}${if (isAccredited) "_certified" else ""}")