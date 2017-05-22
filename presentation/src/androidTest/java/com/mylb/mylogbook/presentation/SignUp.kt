package com.mylb.mylogbook.presentation

import android.content.Intent
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.mylb.mylogbook.presentation.test.espresso.facade.Actions.Companion.clearThenType
import com.mylb.mylogbook.presentation.test.espresso.facade.Actions.Companion.click
import com.mylb.mylogbook.presentation.test.espresso.facade.Actions.Companion.type
import com.mylb.mylogbook.presentation.test.espresso.facade.Assertions.Companion.seeDialog
import com.mylb.mylogbook.presentation.test.espresso.facade.Assertions.Companion.seeError
import com.mylb.mylogbook.presentation.test.espresso.facade.Assertions.Companion.seeFieldIsRequired
import com.mylb.mylogbook.presentation.test.espresso.facade.Assertions.Companion.seeNoError
import com.mylb.mylogbook.presentation.test.espresso.facade.Assertions.Companion.seeToast
import com.mylb.mylogbook.presentation.test.espresso.resource.OkHttpResource
import com.mylb.mylogbook.presentation.ui.activity.auth.LogInActivity
import com.mylb.mylogbook.presentation.ui.activity.auth.SignUpActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.hasItem
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

    @Before
    fun setUp() {
        okHttpResource = OkHttpResource(activity.activity.httpClient)
        Espresso.registerIdlingResources(okHttpResource)
    }

    @After
    fun tearDown() { Espresso.unregisterIdlingResources(okHttpResource) }

    @Test
    fun i_can_sign_up() {
        seeFieldIsRequired(nameTextInputLayout, nameEditText)
        type(nameEditText, "John")
        seeNoError(nameTextInputLayout)

        val email = generateUniqueEmail()
        seeFieldIsRequired(emailTextInputLayout, emailEditText)
        type(emailEditText, email.substring(0..3))
        seeError(emailTextInputLayout, "Must be a valid email")
        type(emailEditText, email.substring(4))
        seeNoError(emailTextInputLayout)

        seeFieldIsRequired(passwordTextInputLayout, passwordEditText)
        type(passwordEditText, "sec")
        seeError(passwordTextInputLayout, "Must be at least 6 characters")
        type(passwordEditText, "secret")
        seeNoError(passwordTextInputLayout)

        seeFieldIsRequired(birthdateTextInputLayout, birthdateEditText)
        type(birthdateEditText, "2000")
        seeError(birthdateTextInputLayout, "YYYY-MM-DD")
        type(birthdateEditText, "-10-10")
        seeNoError(birthdateTextInputLayout)

        click(withId(submitButton))
        seeDialog(R.string.sign_up_success_title)

        click(withText(R.string.log_in))
        intended(hasComponent(LogInActivity::class.java.name))
    }

    @Test
    fun i_can_see_my_email_is_taken() {
        fillInForm()
        clearThenType(emailEditText, "dev@mlb.com")
        click(withId(submitButton))
        seeToast(R.string.error_email_taken)
    }

    @Test
    fun i_can_go_to_my_mail_box_after_signing_up() {
        fillInForm()
        click(withId(submitButton))
        seeDialog(R.string.sign_up_success_title)
        click(withText(R.string.open_mail))
        intended(allOf(
                hasAction(Intent.ACTION_MAIN),
                hasCategories(hasItem(Intent.CATEGORY_APP_EMAIL))
        ))
    }

    private fun fillInForm() {
        type(nameEditText, "John")
        type(emailEditText, generateUniqueEmail())
        type(passwordEditText, "secret")
        type(birthdateEditText, "2000-10-10")
    }

    private fun generateUniqueEmail(): String {
        val number = System.currentTimeMillis() * 1000

        return "john_doe_$number@mlb.com"
    }
}
