package com.mylb.mylogbook.presentation.ui.activity

import android.graphics.Color
import android.os.Bundle
import com.mylb.mylogbook.domain.delivery.web.Response
import com.mylb.mylogbook.domain.interactor.auth.CheckAuthentication
import com.mylb.mylogbook.domain.interactor.auth.CheckAuthentication.Params.Credential
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.di.component.AuthComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAuthComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.activity.auth.LogInActivity
import com.mylb.mylogbook.presentation.ui.activity.auth.SignUpActivity
import com.mylb.mylogbook.presentation.ui.activity.dashboard.DashboardActivity
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject lateinit var checkAuthentication: CheckAuthentication

    override val presenter: Presenter? = null

    private val component: AuthComponent
        get() = DaggerAuthComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(activityModule)
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component.inject(this)

        if (cache.apiToken != null) redirectToDashboard()

        supportActionBar?.hide()

        window.statusBarColor = Color.TRANSPARENT

        signUpButton.setOnClickListener { SignUpActivity.start(this) }
        logInButton.setOnClickListener { LogInActivity.start(this) }
    }

    private fun redirectToDashboard() {
        checkAuthentication.execute(CheckAuthenticationObserver(), Credential(cache.email!!))

        DashboardActivity.start(this)

        finish()
    }

    private inner class CheckAuthenticationObserver : DisposableObserver<Response<Unit>>() {
        override fun onNext(t: Response<Unit>?) = Unit
        override fun onComplete() = Unit
        override fun onError(e: Throwable?) = Unit
    }
}