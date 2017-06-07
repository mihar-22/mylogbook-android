package com.mylb.mylogbook.presentation.validation

import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView
import io.reactivex.Observable
import io.reactivex.rxkotlin.combineLatest

sealed class Validator {

    class Required : Validator(), ValidationRule {
        override val errorMessage = "This field is required"

        override fun validate(value: CharSequence): Boolean {
            return value.isNotEmpty()
        }
    }

    class Min(val min: Int = 0) : Validator(), ValidationRule {
        override val errorMessage = "Must be at least $min"

        override fun validate(value: CharSequence): Boolean {
            if (value.isEmpty()) return false

            return value.toString().toInt() >= min
        }
    }

    class Max(val max: Int = 0) : Validator(), ValidationRule {
        override val errorMessage = "Must be no greater than $max"

        override fun validate(value: CharSequence): Boolean {
            if (value.isEmpty()) return true

            return value.toString().toInt() <= max
        }
    }

    class MinLength(val min: Int = 0) : Validator(), ValidationRule {
        override val errorMessage = "Must be at least $min characters"

        override fun validate(value: CharSequence): Boolean {
            return value.length >= min
        }
    }

    class MaxLength(val max: Int = 0) : Validator(), ValidationRule {
        override val errorMessage = "Must no greater than $max characters"

        override fun validate(value: CharSequence): Boolean {
            return value.length <= max
        }
    }

    class Email : Validator(), ValidationRule {
        override val errorMessage = "Must be a valid email"

        override fun validate(value: CharSequence): Boolean {
            val pattern = "^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"

            return kotlin.text.Regex(pattern).matches(value)
        }
    }

    class Regex(val pattern: CharSequence, error: CharSequence) : Validator(), ValidationRule {
        override val errorMessage = error

        override fun validate(value: CharSequence): Boolean {
            return kotlin.text.Regex(pattern.toString()).matches(value)
        }
    }

    class Date() : Validator(), ValidationRule {
        override val errorMessage = "YYYY-MM-DD"

        override fun validate(value: CharSequence): Boolean {
            val pattern = "^(19|20)\\d{2}[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])$"

            return kotlin.text.Regex(pattern).matches(value)
        }
    }

    companion object {
        inline fun <reified T : Enum<T>> validationChanges(
                view: ValidatingView<T>,
                crossinline rules: (T) -> ArrayList<ValidationRule>
        ): Observable<Boolean> {

            val validations = ArrayList<Observable<Boolean>>()

            enumValues<T>().forEach { field ->
                val fieldValidationChanges = view.textChanges(field)
                        .skip(1)
                        .map { it.trim() }
                        .map { value -> Validator.validate(value, rules(field)) }
                        .doOnNext { errors -> view.showError(field, errors.firstOrNull()) }
                        .map { it.isEmpty() }

                validations.add(fieldValidationChanges)
            }

            return validations.combineLatest { !it.contains(false) }
        }

        fun validate(
                value: CharSequence,
                rules: ArrayList<ValidationRule>
        ): ArrayList<CharSequence> {

            val errors = ArrayList<CharSequence>()

            rules.forEach { rule -> if (!rule.validate(value)) errors.add(rule.errorMessage) }

            return errors
        }
    }
}