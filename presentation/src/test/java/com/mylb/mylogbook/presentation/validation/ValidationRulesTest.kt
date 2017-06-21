package com.mylb.mylogbook.presentation.validation

import org.amshove.kluent.*
import org.junit.Test

class ValidationRulesTest {

    @Test
    fun Required_InputProvided_True() {
        val value = "a"

        val isValid = Validator.Required().validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Required_EmptyInput_False() {
        val value = ""

        val isValid = Validator.Required().validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun Min_NumberGreaterThanMin_True() {
        val value = "4"

        val isValid = Validator.Min(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Min_NumberEqualToMin_True() {
        val value = "2"

        val isValid = Validator.Min(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Min_NumberLessThanMin_False() {
        val value = "1"

        val isValid = Validator.Min(2).validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun Min_Empty_False() {
        val value = ""

        val isValid = Validator.Min(2).validate(value)

        isValid.shouldBeFalse()
    }

    @Test(expected = NumberFormatException::class)
    fun Min_Alpha_NumberFormatException() {
        val value = "a"

        Validator.Min(2).validate(value)
    }

    @Test
    fun Min_EmptyInput_True() {
        val value = "2"

        val isValid = Validator.Min(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Max_NumberLessThanMax_True() {
        val value = "2"

        val isValid = Validator.Max(4).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Max_NumberEqualToMax_True() {
        val value = "4"

        val isValid = Validator.Max(4).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Max_NumberGreaterThanMax_False() {
        val value = "4"

        val isValid = Validator.Max(2).validate(value)

        isValid.shouldBeFalse()
    }

    @Test(expected = NumberFormatException::class)
    fun Max_Alpha_NumberFormatException() {
        val value = "a"

        Validator.Max(4).validate(value)
    }

    @Test
    fun Max_EmptyInput_True() {
        val value = ""

        val isValid = Validator.Max(4).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun MinLength_LengthGreaterThanMin_True() {
        val value = "abc"

        val isValid = Validator.MinLength(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun MinLength_LengthEqualToMin_True() {
        val value = "ab"

        val isValid = Validator.MinLength(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun MinLength_LengthLessThanMin_False() {
        val value = "a"

        val isValid = Validator.MinLength(2).validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun MaxLength_LengthLessThanMax_True() {
        val value = "ab"

        val isValid = Validator.MaxLength(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun MaxLength_LengthGreaterThanMax_False() {
        val value = "abc"

        val isValid = Validator.MaxLength(2).validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun Email_ValidEmail_True() {
        val email = "john_doe@gmail.com"

        val isValid = Validator.Email().validate(email)

        isValid.shouldBeTrue()
    }

    @Test
    fun Email_InvalidEmail_False() {
        val email = "invalidgmail.com"

        val isValid = Validator.Email().validate(email)

        isValid.shouldBeFalse()
    }

    @Test
    fun Regex_ValidInput_True() {
        val pattern = "^\\d{4}$"
        val value = "2222"

        val isValid = Validator.Regex(pattern, "error").validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Regex_InvalidInput_False() {
        val pattern = "^\\d{4}$"
        val value = "2"

        val isValid = Validator.Regex(pattern, "error").validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun Date_ValidDateFormat_True() {
        val date = "1993-09-10"

        val isValid = Validator.Date().validate(date)

        isValid.shouldBeTrue()
    }

    @Test
    fun Date_InvalidDateFormat_False() {
        val date = "10-09-1993"

        val isValid = Validator.Date().validate(date)

        isValid.shouldBeFalse()
    }

    @Test
    fun Validate_ValidInput_ErrorsEmpty() {
        val value = "johnDoe"

        val rules: ArrayList<ValidationRule> = arrayListOf(
                Validator.Required(),
                Validator.MinLength(6)
        )

        val errors = Validator.validate(value, rules)

        errors.shouldBeEmpty()
    }

    @Test
    fun Validate_InvalidInput_ErrorsNotEmpty() {
        val value = "john"

        val rules: ArrayList<ValidationRule> = arrayListOf(
                Validator.Required(),
                Validator.MinLength(6)
        )

        val errors = Validator.validate(value, rules)

        errors.shouldNotBeEmpty()
    }

}
