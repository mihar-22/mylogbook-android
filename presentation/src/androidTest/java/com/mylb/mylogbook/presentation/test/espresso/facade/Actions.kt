package com.mylb.mylogbook.presentation.test.espresso.facade

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

class Actions {
    companion object {

        @JvmStatic fun type(id: Int, text: String) {
            onView(withId(id)).perform(typeText(text))
        }

        @JvmStatic fun clearThenType(id: Int, text: String) {
            onView(withId(id)).perform(clearText(), typeText(text))
        }

        @JvmStatic fun click(matcher: Matcher<View>) {
            onView(allOf(matcher, isEnabled())).perform(click())
        }
    }
}
