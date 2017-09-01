package com.mylb.mylogbook.presentation.ui.activity.setup

import android.os.Bundle
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.clicks
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.learner.AustralianState
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.di.component.AndroidComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAndroidComponent
import com.mylb.mylogbook.presentation.di.module.AndroidModule
import com.mylb.mylogbook.presentation.presenter.setup.SetupStatePresenter
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity
import com.mylb.mylogbook.presentation.ui.activity.MainActivity
import com.mylb.mylogbook.presentation.ui.view.setup.SetupStateView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_setup_state.*
import me.eugeniomarletti.extras.SimpleActivityCompanion
import timber.log.Timber
import javax.inject.Inject

class SetupStateActivity : BaseActivity(), SetupStateView {

    @Inject lateinit var userCache: UserCache
    @Inject override lateinit var presenter: SetupStatePresenter

    private val component: AndroidComponent
        get() = DaggerAndroidComponent.builder()
                .applicationComponent(applicationComponent)
                .androidModule(AndroidModule(this))
                .build()

    override val submitButtonClicks: Observable<Unit>
        get() = nextButton.clicks()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_state)

        component.inject(this)
        presenter.view = this

        setupStateSpinner()
    }

    private fun setupStateSpinner() {
        val adapter = ArrayAdapter<String>(
                this,
                R.layout.spinner_item,
                AustralianState.values().map { it.displayName }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        stateSpinner.adapter = adapter
    }

    override fun navigateToDashboard() {
        Timber.d("Navigating to dashboard")

        val state = AustralianState.values()[stateSpinner.selectedItemPosition]

        userCache.state = state

        MainActivity.start(this)

        finish()
    }

    companion object : SimpleActivityCompanion(SetupStateActivity::class)

}
