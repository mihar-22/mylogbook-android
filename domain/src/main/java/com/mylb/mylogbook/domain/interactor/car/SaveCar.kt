package com.mylb.mylogbook.domain.interactor.car

import com.mylb.mylogbook.domain.delivery.local.repository.LocalCarRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.interactor.BaseUseCase
import com.mylb.mylogbook.domain.resource.Car
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SaveCar<T : Car> @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        disposables: CompositeDisposable,
        private val localRepository: LocalCarRepository<T>
) : BaseUseCase<Unit, T>(
        threadExecutor,
        postExecutionThread,
        disposables
) {

    override fun buildObservable(params: T): Observable<Unit> {
        val cars = arrayListOf<T>(params)

        return if (params.id == 0)
            Observable.defer { Observable.just(localRepository.insert(cars)) }
        else
            Observable.defer { Observable.just(localRepository.update(cars)) }
    }

}

