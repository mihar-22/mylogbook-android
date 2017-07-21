package com.mylb.mylogbook.presentation.validation

interface ValidatableForm {

    fun rules(): List<ValidationRule>?

}

