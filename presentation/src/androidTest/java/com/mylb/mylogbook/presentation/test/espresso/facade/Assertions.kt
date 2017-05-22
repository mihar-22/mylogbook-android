package com.mylb.mylogbook.presentation.test.espresso.facade

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import com.mylb.mylogbook.presentation.test.espresso.ViewAssertions.Companion.hasError
import com.mylb.mylogbook.presentation.test.espresso.ViewAssertions.Companion.noError
import com.mylb.mylogbook.presentation.test.espresso.ViewMatchers.Companion.isToast

class Assertions {
    companion object {

        @JvmStatic fun seeError(id: Int, error: CharSequence) {
            onView(withId(id)).check(matches(hasError(error)))
        }

        @JvmStatic fun seeNoError(id: Int) {
            onView(withId(id)).check(matches(noError()))
        }

        @JvmStatic fun seeFieldIsRequired(textInputLayoutId: Int, editTextId: Int) {
            onView(withId(editTextId)).perform(typeText("0"), clearText())

            seeError(textInputLayoutId, "This field is required")
        }

        @JvmStatic fun seeToast(id: Int) {
            onView(withText(id)).inRoot(isToast()).check(matches(isDisplayed()))
        }

        @JvmStatic fun seeDialog(id: Int) {
            onView(withText(id)).inRoot(isDialog()).check(matches(isDisplayed()))
        }
    }
}
