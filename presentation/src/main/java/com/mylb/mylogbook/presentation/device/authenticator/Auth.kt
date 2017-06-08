package com.mylb.mylogbook.presentation.device.authenticator

import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mylb.mylogbook.domain.auth.Auth
import timber.log.Timber

class Auth constructor(context: Context) : Auth {

    private val accountManager = AccountManager.get(context)

    private var authenticatorIntent: Intent? = null

    private var authenticatorResponse: AccountAuthenticatorResponse? = null
        set(response) {
            field = response

            field?.onRequestContinued()
        }

    override fun <T> processRequest(request: T) {
        Timber.d("Processing request")

        if (request !is Intent) {
            Timber.d("Unknown request type")

            throw ClassCastException("Could not cast request to Intent")
        }

        with (Authenticator.IntentOptions) {
            authenticatorResponse = request.authenticatorResponse

            if (authenticatorResponse == null) return

            authenticatorIntent = Intent()
            authenticatorIntent!!.accountName = request.accountName
            authenticatorIntent!!.accountType = request.accountType ?: ACCOUNT_TYPE
        }
    }

    override fun setToken(name: String, token: String) {
        Timber.d("Setting authentication token for %s", name)

        val account = getAccount(name)

        accountManager.setAuthToken(account, TOKEN_TYPE_FULL_ACCESS, token)

        with (Authenticator.IntentOptions) { authenticatorIntent?.authToken = token }

        finish()
    }

    override fun removeAccount(name: String) {
        Timber.d("Removing %s account", name)

        val account = getAccount(name)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            accountManager.removeAccountExplicitly(account)
        else
            accountManager.removeAccount(account, null, null)
    }

    fun hasAccount(): Boolean = accountManager.accounts.isNotEmpty()

    fun peekToken(account: Account): String? = accountManager.peekAuthToken(account, TOKEN_TYPE_FULL_ACCESS)

    private fun finish() {
        Timber.d("Finishing")

        if (authenticatorIntent != null) {
            Timber.d("Returning result to authenticator")

            authenticatorResponse?.onResult(authenticatorIntent!!.extras)
        } else {
            Timber.d("Returning cancelled error to authenticator")

            authenticatorResponse?.onError(AccountManager.ERROR_CODE_CANCELED, "Canceled")
        }

        destroy()
    }

    private fun getAccount(name: String): Account {
        val account = Account(name, ACCOUNT_TYPE)

        if (!accountExists(account)) {
            Timber.d("Adding account explicitly")

            accountManager.addAccountExplicitly(account, null, null)
        }

        return account
    }

    private fun accountExists(account: Account): Boolean = accountManager.accounts.contains(account)

    private fun destroy() {
        Timber.d("Destroying")

        authenticatorIntent = null
        authenticatorResponse = null
    }

    companion object {
        const val ACCOUNT_TYPE = "com.mylb.mylogbook.account"

        const val TOKEN_TYPE_FULL_ACCESS = "Full access"
        const val TOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to a Mylogbook account"

        const val ERROR_CODE_ONLY_ONE_ACCOUNT = 1
    }
}
