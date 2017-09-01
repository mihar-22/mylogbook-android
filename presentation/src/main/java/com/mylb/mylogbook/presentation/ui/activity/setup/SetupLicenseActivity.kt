package com.mylb.mylogbook.presentation.ui.activity.setup

import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.di.component.AndroidComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAndroidComponent
import com.mylb.mylogbook.presentation.di.module.AndroidModule
import com.mylb.mylogbook.presentation.presenter.setup.SetupLicensePresenter
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity
import com.mylb.mylogbook.presentation.ui.view.setup.SetupLicenseView
import com.mylb.mylogbook.presentation.ui.view.setup.SetupLicenseView.FormField.RECEIVED_ON
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_setup_license.*
import me.eugeniomarletti.extras.SimpleActivityCompanion
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject

class SetupLicenseActivity : BaseActivity(), SetupLicenseView {

    @Inject lateinit var userCache: UserCache
    @Inject override lateinit var presenter: SetupLicensePresenter

    private val component: AndroidComponent
        get() = DaggerAndroidComponent.builder()
                .applicationComponent(applicationComponent)
                .androidModule(AndroidModule(this))
                .build()

    override val submitButtonClicks: Observable<Unit>
        get() = nextButton.clicks()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_license)

        component.inject(this)
        presenter.view = this
    }

    override fun onBackPressed() { moveTaskToBack(true) }

    override fun enableSubmitButton(isEnabled: Boolean) { nextButton.isEnabled = isEnabled }

    override fun textInputLayout(field: SetupLicenseView.FormField) = when (field) {
        RECEIVED_ON -> licenceReceivedTextInputLayout
    }

    override fun navigateToStateSetup() {
        Timber.d("Navigating to state setup")

        val receivedOn = licenceReceivedEditText.text.trim().toString()

        userCache.receivedLicenseDate = if (receivedOn.isNotEmpty()) {
            DateTime.parse(receivedOn, Network.dateFormat)
        } else {
            DateTime.parse(DateTime.now().toString(Network.dateFormat), Network.dateFormat)
        }

        SetupStateActivity.start(this)
    }

    companion object : SimpleActivityCompanion(SetupLicenseActivity::class)

}
