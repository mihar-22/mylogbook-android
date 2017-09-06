package com.mylb.mylogbook.domain.interactor.auth

import com.mylb.mylogbook.domain.auth.Auth
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteAuthRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LogUserOut @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val auth: Auth,
        private val repository: RemoteAuthRepository,
        private val userCache: UserCache
) : BaseUseCase<Response<Unit>, Unit>(
        threadExecutor, postExecutionThread, disposables
) {

    override fun buildObservable(params: Unit): Observable<Response<Unit>> {
        auth.removeAccount(userCache.email!!)

        return repository.logout()
    }

}
