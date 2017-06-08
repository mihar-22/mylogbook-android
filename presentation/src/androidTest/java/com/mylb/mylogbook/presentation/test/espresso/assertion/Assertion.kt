package com.mylb.mylogbook.presentation.test.espresso.assertion

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import android.view.View
import com.mylb.mylogbook.presentation.test.espresso.core.hasError
import com.mylb.mylogbook.presentation.test.espresso.core.isToast
import com.mylb.mylogbook.presentation.test.espresso.core.noError
import org.hamcrest.Matcher

fun seeError(id: Int, error: CharSequence) {
    onView(withId(id)).check(matches(hasError(error)))
}

fun seeNoError(id: Int) {
    onView(withId(id)).check(matches(noError()))
}

fun seeFieldIsRequired(textInputLayoutId: Int, editTextId: Int) {
    onView(withId(editTextId)).perform(typeText("0"), clearText())

    seeError(textInputLayoutId, "This field is required")
}

fun seeToast(id: Int) {
    onView(withText(id)).inRoot(isToast()).check(matches(isDisplayed()))
}

fun seeDialog(id: Int) {
    onView(withText(id)).inRoot(isDialog()).check(matches(isDisplayed()))
}

fun hasText(id: Int, text: String) { onView(withId(id)).check(matches(withText(text))) }

fun check(id: Int, condition: Matcher<View>) { check(withId(id), condition) }

fun check(matcher: Matcher<View>, condition: Matcher<View>) {
    onView(matcher).check(matches(condition))
}