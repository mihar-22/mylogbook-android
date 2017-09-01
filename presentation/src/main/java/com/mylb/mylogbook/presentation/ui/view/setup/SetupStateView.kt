package com.mylb.mylogbook.presentation.ui.view.setup

import io.reactivex.Observable

interface SetupStateView {

    val submitButtonClicks: Observable<Unit>

    fun navigateToDashboard()

}
