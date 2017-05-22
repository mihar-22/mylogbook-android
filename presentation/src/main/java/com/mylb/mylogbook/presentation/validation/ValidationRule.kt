package com.mylb.mylogbook.presentation.validation

interface ValidationRule {
    val errorMessage: CharSequence

    fun validate(value: CharSequence): Boolean
}
