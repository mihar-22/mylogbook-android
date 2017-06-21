package com.mylb.mylogbook.presentation.presenter.auth

import com.mylb.mylogbook.domain.interactor.auth.SignUpUser
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.amshove.kluent.any
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignUpPresenterTest {

    lateinit private var presenter: SignUpPresenter
    lateinit private var testDisposables: CompositeDisposable

    @Mock lateinit private var mockUseCase: SignUpUser
    @Mock lateinit private var mockView: SignUpView

    @Before
    fun setUp() {
        testDisposables = CompositeDisposable()

        given(mockView.text(any())).willReturn("")
        given(mockView.textChanges(any())).willReturn(Observable.empty())
        given(mockView.submitButtonClicks).willReturn(Observable.just(Unit))

        presenter = SignUpPresenter(mockUseCase, testDisposables)
        presenter.view = mockView
    }

    @Test
    fun SetView_Null_DisposablesCleared() {
        testDisposables.size().shouldBe(2)

        presenter.view = null

        testDisposables.size().shouldBe(0)
    }

    @Test
    fun Destroy_Unit_DisposablesDisposedAndViewNull() {
        presenter.destroy()

        presenter.view.shouldBeNull()
        testDisposables.isDisposed.shouldBeTrue()
    }

    @Test
    fun SetView_MockView_SubscribedToViews() {
        verify(mockView).textChanges(SignUpView.Field.NAME)
        verify(mockView).textChanges(SignUpView.Field.EMAIL)
        verify(mockView).textChanges(SignUpView.Field.PASSWORD)
        verify(mockView).textChanges(SignUpView.Field.BIRTHDATE)
        verify(mockView).submitButtonClicks
    }

    @Test
    fun ObserveSubmitButtonClicks_OnClick_ShowLoadingAndUseCaseExecuted() {
        verify(mockView).showLoading()
        verify(mockUseCase).execute(any(), any())
    }

}