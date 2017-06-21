package com.mylb.mylogbook.presentation.device.authenticator

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.mylb.mylogbook.domain.auth.Auth
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.device.authenticator.Auth.Companion.ERROR_CODE_ONLY_ONE_ACCOUNT
import com.mylb.mylogbook.presentation.device.authenticator.Auth.Companion.TOKEN_TYPE_FULL_ACCESS_LABEL
import com.mylb.mylogbook.presentation.di.qualifier.ForAndroidComponent
import com.mylb.mylogbook.presentation.ui.activity.auth.LogInActivity
import me.eugeniomarletti.extras.ActivityCompanion
import me.eugeniomarletti.extras.intent.IntentExtra
import me.eugeniomarletti.extras.intent.base.Parcelable
import me.eugeniomarletti.extras.intent.base.String
import timber.log.Timber
import javax.inject.Inject

class Authenticator @Inject constructor(
        @ForAndroidComponent val context: Context,
        private val auth: Auth
) : AbstractAccountAuthenticator(context) {

    private val handler = Handler()

    override fun addAccount(
            response: AccountAuthenticatorResponse?,
            accountType: String,
            authTokenType: String?,
            requiredFeatures: Array<out String>?,
            options: Bundle?
    ): Bundle? {
        Timber.d("Adding account")

        if (auth.accountExists()) {
            showAccountExistsToast()

            return errorAccountExistsBundle()
        }

        return userInteractionBundle(null, response)
    }

    override fun getAuthToken(
            response: AccountAuthenticatorResponse,
            account: Account,
            authTokenType: String,
            options: Bundle?
    ): Bundle? {
        Timber.d("Getting authentication token")

        val authToken: String? = auth.peekToken(account)

        if (authToken != null) {
            Timber.d("Authentication token found")

            return authTokenBundle(account, authToken)
        }

        Timber.d("Authentication token not found")

        return userInteractionBundle(account, response)
    }

    override fun getAuthTokenLabel(authTokenType: String) = TOKEN_TYPE_FULL_ACCESS_LABEL

    override fun confirmCredentials(
            response: AccountAuthenticatorResponse?,
            account: Account?,
            options: Bundle?
    ): Bundle? = null

    override fun updateCredentials(
            response: AccountAuthenticatorResponse?,
            account: Account?,
            authTokenType: String?,
            options: Bundle?
    ): Bundle = throw UnsupportedOperationException("")

    override fun hasFeatures(
            response: AccountAuthenticatorResponse?,
            account: Account?,
            features: Array<out String>?
    ): Bundle = throw UnsupportedOperationException("")

    override fun editProperties(
            response: AccountAuthenticatorResponse?,
            accountType: String?
    ): Bundle = throw UnsupportedOperationException("")

    private fun showAccountExistsToast() {
        Timber.d("Account exists")

        handler.post {
            val message = context.getString(R.string.error_one_account_only)

            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun userInteractionBundle(
            account: Account? = null,
            response: AccountAuthenticatorResponse? = null
    ): Bundle {

        val intent = intent(context) {
            it.accountName = account?.name
            it.authenticatorResponse = response
        }

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    private fun authTokenBundle(account: Account, authToken: String): Bundle {
        val intent = intent(context) {
            it.accountName = account.name
            it.accountType = account.type
            it.authToken = authToken
        }

        return intent.extras
    }

    private fun errorAccountExistsBundle(): Bundle {
        val bundle = Bundle()
        bundle.putInt(AccountManager.KEY_ERROR_CODE, ERROR_CODE_ONLY_ONE_ACCOUNT)
        bundle.putString(AccountManager.KEY_ERROR_MESSAGE, context.getString(R.string.error_one_account_only))
        return bundle
    }

    companion object : ActivityCompanion<IntentOptions>(IntentOptions, LogInActivity::class)

    object IntentOptions {

        var Intent.accountName by IntentExtra.String(name = AccountManager.KEY_ACCOUNT_NAME, customPrefix = "")
        var Intent.accountType by IntentExtra.String(name = AccountManager.KEY_ACCOUNT_TYPE, customPrefix = "")
        var Intent.authToken by IntentExtra.String(name = AccountManager.KEY_AUTHTOKEN, customPrefix = "")

        var Intent.authenticatorResponse by IntentExtra.Parcelable<AccountAuthenticatorResponse>(
                name = AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, customPrefix = ""
        )

    }

}
