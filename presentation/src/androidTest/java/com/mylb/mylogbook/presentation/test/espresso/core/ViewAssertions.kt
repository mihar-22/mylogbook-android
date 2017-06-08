package com.mylb.mylogbook.presentation.test.espresso.core

import android.support.design.widget.TextInputLayout
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher

fun hasError(error: CharSequence): Matcher<View> {
    return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {

        override fun matchesSafely(item: TextInputLayout): Boolean {
            val itemError = if (item.error == null) "" else item.error

            return error == itemError
        }

        override fun describeTo(description: Description) {
            description.appendText("has error: ")
        }
    }
}

fun noError(): Matcher<View> {
    return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {

        override fun matchesSafely(item: TextInputLayout): Boolean = (item.error == null)

        override fun describeTo(description: Description) {
            description.appendText("no error")
        }
    }
}
