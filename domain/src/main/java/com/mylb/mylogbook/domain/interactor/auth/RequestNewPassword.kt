package com.mylb.mylogbook.domain.interactor.auth

import com.mylb.mylogbook.domain.delivery.web.Response
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.UseCase
import com.mylb.mylogbook.domain.repository.AuthRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RequestNewPassword @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val repository: AuthRepository
) : UseCase<Response<Unit>, RequestNewPassword.Params.Requester>(
        threadExecutor, postExecutionThread, disposables
) {
    override fun buildObservable(params: Requester) = repository.forgot(params.email)

    companion object Params {
        class Requester(val email: String)
    }
}