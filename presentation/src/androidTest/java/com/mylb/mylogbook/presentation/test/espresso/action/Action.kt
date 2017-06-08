package com.mylb.mylogbook.presentation.test.espresso.action

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

fun type(id: Int, text: String) { onView(withId(id)).perform(typeText(text)) }

fun clear(id: Int) { onView(withId(id)).perform(clearText()) }

fun clearThenType(id: Int, text: String) { onView(withId(id)).perform(clearText(), typeText(text)) }

fun click(id: Int) { click(withId(id)) }

fun click(matcher: Matcher<View>) { onView(allOf(matcher, isEnabled())).perform(click()) }

fun dismiss(id: Int) { onView(withText(id)).perform(pressBack()) }
