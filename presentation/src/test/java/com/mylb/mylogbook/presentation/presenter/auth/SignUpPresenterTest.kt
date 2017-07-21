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

        given(mockView.formValidationChanges).willReturn(Observable.empty())
        given(mockView.submitButtonClicks).willReturn(Observable.empty())

        presenter = SignUpPresenter(mockUseCase, testDisposables)
    }

    @Test
    fun SetView_Null_DisposablesCleared() {
        presenter.view = mockView

        testDisposables.size().shouldBe(2)

        presenter.view = null

        testDisposables.size().shouldBe(0)
    }

    @Test
    fun Destroy_Unit_DisposablesDisposedAndViewNull() {
        presenter.view = mockView

        presenter.destroy()

        presenter.view.shouldBeNull()
        testDisposables.isDisposed.shouldBeTrue()
    }

    @Test
    fun SetView_MockView_SubscribedToViews() {
        presenter.view = mockView

        verify(mockView).submitButtonClicks
        verify(mockView).formValidationChanges
    }

    @Test
    fun ObserveSubmitButtonClicks_OnClick_ShowLoadingAndUseCaseExecuted() {
        given(mockView.submitButtonClicks).willReturn(Observable.just(Unit))

        presenter.view = mockView

        verify(mockView).showLoading()
        verify(mockUseCase).execute(any(), any())
    }

    @Test
    fun FormValidationChanges_True_SubmitButtonEnabled() {
        given(mockView.formValidationChanges).willReturn(Observable.just(true))

        presenter.view = mockView

        verify(mockView).enableSubmitButton(true)
    }

    @Test
    fun FormValidationChanges_False_SubmitButtonDisabled() {
        given(mockView.formValidationChanges).willReturn(Observable.just(false))

        presenter.view = mockView

        verify(mockView).enableSubmitButton(false)
    }

}