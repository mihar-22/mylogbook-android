package com.mylb.mylogbook.domain.interactor.supervisor

import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import com.mylb.mylogbook.domain.resource.Supervisor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SaveSupervisor<T : Supervisor> @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val localRepository: LocalSupervisorRepository<T>
) : BaseUseCase<Unit, T>(
        threadExecutor,
        postExecutionThread,
        disposables
) {

    override fun buildObservable(params: T): Observable<Unit> {
        val supervisors = listOf(params)

        return if (params.id == 0)
            Observable.defer { Observable.just(localRepository.insert(supervisors)) }
        else
            Observable.defer { Observable.just(localRepository.update(supervisors)) }
    }

}

