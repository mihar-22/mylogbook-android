package com.mylb.mylogbook.domain.interactor.log

import com.mylb.mylogbook.domain.delivery.local.repository.LocalTripRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import com.mylb.mylogbook.domain.resource.Trip
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SaveTrip<T : Trip> @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val localRepository: LocalTripRepository<T>
) : BaseUseCase<Unit, T>(
        threadExecutor,
        postExecutionThread,
        disposables
) {

    override fun buildObservable(params: T): Observable<Unit> {
        val trips = listOf(params)

        return Observable.defer { Observable.just(localRepository.insert(trips)) }
    }

}

