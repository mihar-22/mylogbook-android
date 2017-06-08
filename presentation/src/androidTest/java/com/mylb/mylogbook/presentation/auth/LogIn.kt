package com.mylb.mylogbook.presentation.auth

import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.test.espresso.action.clear
import com.mylb.mylogbook.presentation.test.espresso.action.click
import com.mylb.mylogbook.presentation.test.espresso.action.type
import com.mylb.mylogbook.presentation.test.espresso.assertion.*
import com.mylb.mylogbook.presentation.test.espresso.resource.OkHttpResource
import com.mylb.mylogbook.presentation.ui.activity.auth.LogInActivity
import com.mylb.mylogbook.presentation.ui.activity.dashboard.DashboardActivity
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LogIn {

    @Rule @JvmField var activity = IntentsTestRule(LogInActivity::class.java)

    lateinit var okHttpResource: OkHttpResource

    private val emailTextInputLayout = R.id.emailTextInputLayout
    private val passwordTextInputLayout = R.id.passwordTextInputLayout

    private val emailEditText = R.id.emailEditText
    private val passwordEditText = R.id.passwordEditText

    private val submitButton = R.id.submitButton
    private val forgotPasswordButton = R.id.forgotPasswordButton

    private val user = User("dev@mlb.com", "secret")

    @Before
    fun setUp() {
        okHttpResource = OkHttpResource(activity.activity.httpClient)
        Espresso.registerIdlingResources(okHttpResource)
    }

    @After
    fun tearDown() { Espresso.unregisterIdlingResources(okHttpResource) }

    @Test
    fun i_can_log_in() {
        seeFieldIsRequired(emailTextInputLayout, emailEditText)
        type(emailEditText, user.email.substring(0..3))
        seeError(emailTextInputLayout, "Must be a valid email")
        type(emailEditText, user.email.substring(4))
        seeNoError(emailTextInputLayout)

        seeFieldIsRequired(passwordTextInputLayout, passwordEditText)
        type(passwordEditText, user.password.substring(0..3))
        seeError(passwordTextInputLayout, "Must be at least 6 characters")
        type(passwordEditText, user.password.substring(4))
        seeNoError(passwordTextInputLayout)

        click(submitButton)
        intended(hasComponent(DashboardActivity::class.java.name))
    }

    @Test
    fun i_can_submit_a_forgot_password_request() {
        clear(emailEditText)
        check(forgotPasswordButton, not(isEnabled()))
        type(emailEditText, user.email)
        click(forgotPasswordButton)
        seeDialog(R.string.password_reset_sent_title)
    }

    private class User(val email: String, val password: String)
}
