package com.mylb.mylogbook.presentation.validation

sealed class Rule : ValidationRule {

    class Required : Rule() {
        override val errorMessage = "This field is required"

        override fun validate(value: CharSequence) = value.isNotEmpty()
    }

    class Number : Rule() {
        override val errorMessage = "Only numbers are allowed"

        override fun validate(value: CharSequence): Boolean {
            try {
                value.toString().toInt()

                return true
            } catch (e: NumberFormatException) { return false }
        }

    }

    class Min(val min: Int = 0) : Rule() {
        override val errorMessage = "Must be at least $min"

        override fun validate(value: CharSequence): Boolean {
            if (value.isEmpty()) return false

            val number = value.toString().replace(",", "")

            return Rule.Number().validate(number) && number.toInt() >= min
        }
    }

    class Max(val max: Int = 0) : Rule() {
        override val errorMessage = "Must be no greater than $max"

        override fun validate(value: CharSequence): Boolean {
            if (value.isEmpty()) return true

            val number = value.toString().replace(",", "")

            return Rule.Number().validate(number) && number.toInt() <= max
        }
    }

    class MinLength(val min: Int = 0) : Rule() {
        override val errorMessage = "Must be at least $min characters"

        override fun validate(value: CharSequence) = (value.length >= min)
    }

    class MaxLength(val max: Int = 0) : Rule() {
        override val errorMessage = "Must no greater than $max characters"

        override fun validate(value: CharSequence) = (value.length <= max)
    }

    class Email : Rule() {
        override val errorMessage = "Must be a valid email"

        override fun validate(value: CharSequence): Boolean {
            val pattern = "^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"

            return kotlin.text.Regex(pattern).matches(value)
        }
    }

    class AlphaNum : Rule() {
        override val errorMessage = "Must only contain letters and numbers"

        override fun validate(value: CharSequence): Boolean {
            val pattern = "^[a-zA-Z0-9]*$"

            return kotlin.text.Regex(pattern).matches(value)
        }
    }

    class Regex(val pattern: CharSequence, error: CharSequence) : Rule() {
        override val errorMessage = error

        override fun validate(value: CharSequence) = kotlin.text.Regex(pattern.toString()).matches(value)
    }

    class Date() : Rule() {
        override val errorMessage = "YYYY-MM-DD"

        override fun validate(value: CharSequence): Boolean {
            val pattern = "^(19|20)\\d{2}[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])$"

            return kotlin.text.Regex(pattern).matches(value)
        }
    }

}