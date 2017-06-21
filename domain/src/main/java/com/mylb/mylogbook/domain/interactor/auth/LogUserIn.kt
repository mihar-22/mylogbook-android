package com.mylb.mylogbook.domain.interactor.auth

import com.mylb.mylogbook.domain.auth.Auth
import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteAuthRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LogUserIn @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val auth: Auth,
        private val repository: RemoteAuthRepository
) : BaseUseCase<Response<Map<String, String>>, LogUserIn.Params.Credentials>(
        threadExecutor, postExecutionThread, disposables
) {

    override fun buildObservable(params: Params.Credentials) =
            repository.login(params.email, params.password)
                    .doOnNext { response ->
                        val data = response.data

                        if (data != null) { auth.setToken(params.email, data["api_token"]!!) }
                    }

    companion object Params {

        class Credentials(val email: String, val password: String)

    }

}