package com.mylb.mylogbook.presentation.ui.activity

import android.graphics.Color
import android.os.Bundle
import com.mylb.mylogbook.domain.auth.Auth
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.interactor.auth.CheckAuthentication
import com.mylb.mylogbook.domain.interactor.auth.CheckAuthentication.Params.Credential
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.di.component.AuthComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAuthComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.activity.auth.LogInActivity
import com.mylb.mylogbook.presentation.ui.activity.auth.SignUpActivity
import com.mylb.mylogbook.presentation.ui.activity.setup.SetupLicenseActivity
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_launch.*
import timber.log.Timber
import javax.inject.Inject

class LaunchActivity : BaseActivity() {

    @Inject lateinit var auth: Auth
    @Inject lateinit var userCache: UserCache
    @Inject lateinit var checkAuthentication: CheckAuthentication

    override val presenter: Presenter? = null

    private val component: AuthComponent
        get() = DaggerAuthComponent.builder()
                .applicationComponent(applicationComponent)
                .androidModule(activityModule)
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        component.inject(this)

        if (auth.isAuthenticated() && userCache.apiToken != null) {
            redirectToMain()

            return
        }

        Timber.d("User is not authenticated")

        userCache.destroy()

        supportActionBar?.hide()

        window.statusBarColor = Color.TRANSPARENT

        signUpButton.setOnClickListener { SignUpActivity.start(this) }
        logInButton.setOnClickListener { LogInActivity.start(this) }
    }

    private fun redirectToMain() {
        Timber.d("User is authenticated")

        checkAuthentication.execute(CheckAuthenticationObserver(), Credential(userCache.email!!))

        if (userCache.receivedLicenseDate == null || userCache.state == null) {
            Timber.d("Navigating to setup")

            SetupLicenseActivity.start(this)
        } else {
            Timber.d("Navigating to dashboard")

            MainActivity.start(this)
        }

        finish()
    }

    private inner class CheckAuthenticationObserver : DisposableObserver<Response<Unit>>() {
        override fun onNext(t: Response<Unit>) = Unit
        override fun onComplete() = Unit
        override fun onError(e: Throwable) = Unit
    }

}