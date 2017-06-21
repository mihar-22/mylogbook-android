package com.mylb.mylogbook.presentation.auth

import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.test.espresso.action.clearThenType
import com.mylb.mylogbook.presentation.test.espresso.action.click
import com.mylb.mylogbook.presentation.test.espresso.action.dismiss
import com.mylb.mylogbook.presentation.test.espresso.action.type
import com.mylb.mylogbook.presentation.test.espresso.assertion.*
import com.mylb.mylogbook.presentation.test.espresso.resource.OkHttpResource
import com.mylb.mylogbook.presentation.ui.activity.auth.LogInActivity
import com.mylb.mylogbook.presentation.ui.activity.auth.SignUpActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SignUp {

    @Rule @JvmField var activity = IntentsTestRule(SignUpActivity::class.java)

    lateinit var okHttpResource: OkHttpResource

    private val nameTextInputLayout = R.id.nameTextInputLayout
    private val emailTextInputLayout = R.id.emailTextInputLayout
    private val passwordTextInputLayout = R.id.passwordTextInputLayout
    private val birthdateTextInputLayout = R.id.birthdateTextInputLayout

    private val nameEditText = R.id.nameEditText
    private val emailEditText = R.id.emailEditText
    private val passwordEditText = R.id.passwordEditText
    private val birthdateEditText = R.id.birthdateEditText

    private val submitButton = R.id.submitButton

    private val newUser = NewUser("John", "", "secret", "2000-10-10")

    @Before
    fun setUp() {
        okHttpResource = OkHttpResource(activity.activity.httpClient)
        Espresso.registerIdlingResources(okHttpResource)

        newUser.email = generateUniqueEmail()
    }

    @After
    fun tearDown() { Espresso.unregisterIdlingResources(okHttpResource) }

    @Test
    fun i_can_sign_up() {
        seeFieldIsRequired(nameTextInputLayout, nameEditText)
        type(nameEditText, newUser.name)
        seeNoError(nameTextInputLayout)

        seeFieldIsRequired(emailTextInputLayout, emailEditText)
        type(emailEditText, newUser.email.substring(0..3))
        seeError(emailTextInputLayout, "Must be a valid email")
        type(emailEditText, newUser.email.substring(4))
        seeNoError(emailTextInputLayout)

        seeFieldIsRequired(passwordTextInputLayout, passwordEditText)
        type(passwordEditText, newUser.password.substring(0..3))
        seeError(passwordTextInputLayout, "Must be at least 6 characters")
        type(passwordEditText, newUser.password.substring(4))
        seeNoError(passwordTextInputLayout)

        seeFieldIsRequired(birthdateTextInputLayout, birthdateEditText)
        type(birthdateEditText, newUser.birthdate.substring(0..3))
        seeError(birthdateTextInputLayout, "YYYY-MM-DD")
        type(birthdateEditText, newUser.birthdate.substring(4))
        seeNoError(birthdateTextInputLayout)

        click(submitButton)

        intended(hasComponent(LogInActivity::class.java.name))
        seeDialog(R.string.sign_up_success_title)
        dismiss(R.string.sign_up_success_title)

        hasText(emailEditText, newUser.email)
        hasText(passwordEditText, newUser.password)
    }

    @Test
    fun i_can_see_my_email_is_taken() {
        fillInForm()
        clearThenType(emailEditText, "dev@mlb.com")
        click(submitButton)
        seeToast(R.string.error_email_taken)
    }

    private class NewUser(
            val name: String, var email: String, val password: String, val birthdate: String
    )

    private fun fillInForm() {
        type(nameEditText, newUser.name)
        type(emailEditText, newUser.email)
        type(passwordEditText, newUser.password)
        type(birthdateEditText, newUser.birthdate)
    }

    private fun generateUniqueEmail(): String {
        val number = System.currentTimeMillis() * 1000

        return "john_doe_$number@mlb.com"
    }

}
