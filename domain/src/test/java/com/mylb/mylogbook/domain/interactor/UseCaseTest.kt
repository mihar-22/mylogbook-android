package com.mylb.mylogbook.domain.interactor

import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.domain.test.rule.RxTestScheduler
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.BDDMockito.given
import sun.invoke.empty.Empty

@RunWith(MockitoJUnitRunner::class)
class UseCaseTest {
    lateinit private var useCase: TestUseCase

    lateinit private var testObserver: TestDisposableObserver<Any>
    lateinit private var testDisposables: CompositeDisposable

    @Rule @JvmField var testScheduler = RxTestScheduler()

    @Mock lateinit private var threadExecutor: ThreadExecutor
    @Mock lateinit private var postExecutionThread: PostExecutionThread

    @Before
    fun setUp() {
        testDisposables = CompositeDisposable()

        useCase = TestUseCase(threadExecutor, postExecutionThread, testDisposables)
    }

    @Test
    fun Execute_EmptyParams_NothingEmitted() {
        useCase.execute(testObserver, EmptyParams())

        testObserver.valuesCount.shouldEqual(0)
    }

    @Test
    fun Execute_EmptyParams_ObservableAddedToDisposables() {
        useCase.execute(testObserver, EmptyParams())

        testDisposables.size().shouldBe(1)
    }

    private class TestUseCase constructor(
            threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread,
            disposables: CompositeDisposable
    ) : UseCase<Any, EmptyParams>(threadExecutor, postExecutionThread, disposables) {

        override fun buildObservable(params: EmptyParams): Observable<Any> = Observable.empty()
    }

    private class TestDisposableObserver<T> : DisposableObserver<T>() {
        var valuesCount = 0

        override fun onNext(value: T) { valuesCount++ }
        override fun onError(e: Throwable) {}
        override fun onComplete() {}
    }

    private class EmptyParams {}
}
