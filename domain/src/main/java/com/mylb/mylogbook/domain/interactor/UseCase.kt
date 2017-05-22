package com.mylb.mylogbook.domain.interactor

import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

abstract class UseCase<T, Params> protected constructor(
        private val threadExecutor: ThreadExecutor,
        private val postExecutionThread: PostExecutionThread,
        private val disposables: CompositeDisposable
) {

    protected abstract fun buildObservable(params: Params): Observable<T>

    fun execute(observer: DisposableObserver<T>, params: Params) {
        val observable: Observable<T> = buildObservable(params)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)

        disposables.add(observable.subscribeWith(observer))
    }
}
