package com.mylb.mylogbook.domain.interactor.supervisor

import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import com.mylb.mylogbook.domain.resource.Supervisor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DeleteSupervisors<T : Supervisor> @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val localRepository: LocalSupervisorRepository<T>
) : BaseUseCase<Unit, List<T>>(
        threadExecutor,
        postExecutionThread,
        disposables
) {

    override fun buildObservable(params: List<T>): Observable<Unit> =
            Observable.defer { Observable.just(localRepository.delete(params)) }

}

