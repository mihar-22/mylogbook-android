package com.mylb.mylogbook.domain.interactor.log

import com.mylb.mylogbook.domain.delivery.local.repository.LocalTripRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import com.mylb.mylogbook.domain.resource.Trip
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ShowTrips<T : Trip> @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val localRepository: LocalTripRepository<T>
) : BaseUseCase<List<T>, Unit>(
        threadExecutor,
        postExecutionThread,
        disposables
) {

    override fun buildObservable(params: Unit): Observable<List<T>> =
            localRepository.all().firstElement().toObservable()

}

