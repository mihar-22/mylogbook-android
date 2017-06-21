package com.mylb.mylogbook.domain.interactor.auth

import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteAuthRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RequestNewPassword @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val repository: RemoteAuthRepository
) : BaseUseCase<Response<Unit>, RequestNewPassword.Params.Requester>(
        threadExecutor, postExecutionThread, disposables
) {

    override fun buildObservable(params: Requester) = repository.forgot(params.email)

    companion object Params {

        class Requester(val email: String)

    }

}