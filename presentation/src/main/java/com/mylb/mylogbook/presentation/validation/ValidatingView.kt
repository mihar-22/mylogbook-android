package com.mylb.mylogbook.presentation.validation

import android.support.design.widget.TextInputLayout

interface ValidatingView<T> where T : Enum<T>, T : ValidatableForm {

    fun textInputLayout(field: T): TextInputLayout?

}
