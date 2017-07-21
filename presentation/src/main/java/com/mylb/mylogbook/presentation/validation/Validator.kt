package com.mylb.mylogbook.presentation.validation

import android.support.design.widget.TextInputLayout
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.rxkotlin.combineLatest

fun TextInputLayout.validationChanges(rules: List<ValidationRule>): Observable<Boolean> = this.editText!!
        .textChanges()
        .skip(1)
        .map { it.trim() }
        .map { validate(it, rules) }
        .doOnNext { this.error = it.firstOrNull() }
        .doOnNext { this.isErrorEnabled = (!it.isEmpty()) }
        .map { it.isEmpty() }

fun validate(value: CharSequence, rules: List<ValidationRule>): List<CharSequence> {
    val errors = ArrayList<CharSequence>()

    rules.forEach { rule -> if (!rule.validate(value)) errors.add(rule.errorMessage) }

    return errors
}

inline fun <reified T> onFormValidationChanges(
        view: ValidatingView<T>
): Observable<Boolean> where T : Enum<T>, T : ValidatableForm {

    val validations = arrayListOf<Observable<Boolean>>()

    enumValues<T>().forEach { field ->
        val validationChanges = view.textInputLayout(field)?.validationChanges(field.rules() ?: listOf())

        if (validationChanges != null) validations.add(validationChanges)
    }

    return validations.combineLatest { !it.contains(false) }
}