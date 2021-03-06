package com.mylb.mylogbook.domain.interactor.auth

import com.mylb.mylogbook.domain.auth.Auth
import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteAuthRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import javax.inject.Inject

class CheckAuthentication @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val auth: Auth,
        private val repository: RemoteAuthRepository
) : BaseUseCase<Response<Unit>, CheckAuthentication.Params.Credential>(
        threadExecutor, postExecutionThread, disposables
) {

    override fun buildObservable(params: Credential) =
            repository.check().doOnError {
                if (it is HttpException && it.code() == 401) auth.removeAccount(params.email)
            }

    companion object Params {

        class Credential(val email: String)

    }

}
