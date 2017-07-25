package com.mylb.mylogbook.domain.interactor.supervisor

import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import com.mylb.mylogbook.domain.resource.Supervisor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ShowSupervisors<T : Supervisor> @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val localRepository: LocalSupervisorRepository<T>
) : BaseUseCase<List<T>, Unit>(
        threadExecutor,
        postExecutionThread,
        disposables
) {

    override fun buildObservable(params: Unit): Observable<List<T>> =
            localRepository.all().firstElement().toObservable().map { it.filter { it.deletedAt == null } }

}

