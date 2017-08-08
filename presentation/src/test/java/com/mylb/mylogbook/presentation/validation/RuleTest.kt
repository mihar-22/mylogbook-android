package com.mylb.mylogbook.presentation.validation

import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.Test

class RuleTest {

    @Test
    fun Required_InputProvided_True() {
        val value = "a"

        val isValid = Rule.Required().validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Required_EmptyInput_False() {
        val value = ""

        val isValid = Rule.Required().validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun Number_Number_True() {
        val value = "123"

        val isValid = Rule.Number().validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Number_String_False() {
        val value = "string"

        val isValid = Rule.Number().validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun AlphaNum_AlphaNumString_True() {
        val value = "abc123"

        val isValid = Rule.AlphaNum().validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun AlphaNum_StringWithInvalidChar_False() {
        val value = "abc!123"

        val isValid = Rule.AlphaNum().validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun Min_NumberGreaterThanMin_True() {
        val value = "4"

        val isValid = Rule.Min(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Min_NumberEqualToMin_True() {
        val value = "2"

        val isValid = Rule.Min(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Min_NumberLessThanMin_False() {
        val value = "1"

        val isValid = Rule.Min(2).validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun Min_Empty_False() {
        val value = ""

        val isValid = Rule.Min(2).validate(value)

        isValid.shouldBeFalse()
    }

    @Test(expected = NumberFormatException::class)
    fun Min_Alpha_NumberFormatException() {
        val value = "a"

        Rule.Min(2).validate(value)
    }

    @Test
    fun Min_EmptyInput_True() {
        val value = "2"

        val isValid = Rule.Min(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Max_NumberLessThanMax_True() {
        val value = "2"

        val isValid = Rule.Max(4).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Max_NumberEqualToMax_True() {
        val value = "4"

        val isValid = Rule.Max(4).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Max_NumberGreaterThanMax_False() {
        val value = "4"

        val isValid = Rule.Max(2).validate(value)

        isValid.shouldBeFalse()
    }

    @Test(expected = NumberFormatException::class)
    fun Max_Alpha_NumberFormatException() {
        val value = "a"

        Rule.Max(4).validate(value)
    }

    @Test
    fun Max_EmptyInput_True() {
        val value = ""

        val isValid = Rule.Max(4).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun MinLength_LengthGreaterThanMin_True() {
        val value = "abc"

        val isValid = Rule.MinLength(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun MinLength_LengthEqualToMin_True() {
        val value = "ab"

        val isValid = Rule.MinLength(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun MinLength_LengthLessThanMin_False() {
        val value = "a"

        val isValid = Rule.MinLength(2).validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun MaxLength_LengthLessThanMax_True() {
        val value = "ab"

        val isValid = Rule.MaxLength(2).validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun MaxLength_LengthGreaterThanMax_False() {
        val value = "abc"

        val isValid = Rule.MaxLength(2).validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun Email_ValidEmail_True() {
        val email = "john_doe@gmail.com"

        val isValid = Rule.Email().validate(email)

        isValid.shouldBeTrue()
    }

    @Test
    fun Email_InvalidEmail_False() {
        val email = "invalidgmail.com"

        val isValid = Rule.Email().validate(email)

        isValid.shouldBeFalse()
    }

    @Test
    fun Regex_ValidInput_True() {
        val pattern = "^\\d{4}$"
        val value = "2222"

        val isValid = Rule.Regex(pattern, "error").validate(value)

        isValid.shouldBeTrue()
    }

    @Test
    fun Regex_InvalidInput_False() {
        val pattern = "^\\d{4}$"
        val value = "2"

        val isValid = Rule.Regex(pattern, "error").validate(value)

        isValid.shouldBeFalse()
    }

    @Test
    fun Date_ValidDateFormat_True() {
        val date = "1993-09-10"

        val isValid = Rule.Date().validate(date)

        isValid.shouldBeTrue()
    }

    @Test
    fun Date_InvalidDateFormat_False() {
        val date = "10-09-1993"

        val isValid = Rule.Date().validate(date)

        isValid.shouldBeFalse()
    }

}
