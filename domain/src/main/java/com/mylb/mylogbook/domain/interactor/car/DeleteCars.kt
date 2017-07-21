package com.mylb.mylogbook.domain.interactor.car

import com.mylb.mylogbook.domain.delivery.local.repository.LocalCarRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import com.mylb.mylogbook.domain.resource.Car
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DeleteCars<T : Car> @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val localRepository: LocalCarRepository<T>
) : BaseUseCase<Unit, List<T>>(
        threadExecutor,
        postExecutionThread,
        disposables
) {

    override fun buildObservable(params: List<T>): Observable<Unit> =
            Observable.defer { Observable.just(localRepository.delete(params)) }

}
