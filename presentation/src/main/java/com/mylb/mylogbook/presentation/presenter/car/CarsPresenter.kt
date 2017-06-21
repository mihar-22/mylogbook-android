package com.mylb.mylogbook.presentation.presenter.car

import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import javax.inject.Inject

@PerAndroidComponent
class CarsPresenter @Inject constructor() : Presenter {

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {}

}
