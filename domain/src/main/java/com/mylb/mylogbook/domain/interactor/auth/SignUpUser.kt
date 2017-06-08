package com.mylb.mylogbook.domain.interactor.auth

import com.mylb.mylogbook.domain.delivery.web.Response
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.UseCase
import com.mylb.mylogbook.domain.repository.AuthRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SignUpUser @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val repository: AuthRepository
) : UseCase<Response<Unit>, SignUpUser.Params.NewUser>(
        threadExecutor, postExecutionThread, disposables
) {

    override fun buildObservable(params: NewUser) =
            repository.register(params.name, params.email, params.password, params.birthdate)


    companion object Params {
        class NewUser(
                val name: String,
                val email: String,
                val password: String,
                val birthdate: String
        )
    }
}
