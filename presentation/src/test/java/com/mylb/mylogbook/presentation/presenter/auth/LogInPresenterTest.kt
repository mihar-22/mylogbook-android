package com.mylb.mylogbook.presentation.presenter.auth

import com.mylb.mylogbook.domain.interactor.auth.LogUserIn
import com.mylb.mylogbook.domain.interactor.auth.RequestNewPassword
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.times
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
class LogInPresenterTest {

    lateinit private var presenter: LogInPresenter
    lateinit private var testDisposables: CompositeDisposable

    @Mock lateinit private var mockLogUserInUseCase: LogUserIn
    @Mock lateinit private var mockRequestNewPasswordUseCase: RequestNewPassword
    @Mock lateinit private var mockView: LogInView

    @Before
    fun setUp() {
        testDisposables = CompositeDisposable()

        given(mockView.text(any())).willReturn("")

        given(mockView.emailValidationChanges).willReturn(Observable.empty())
        given(mockView.formValidationChanges).willReturn(Observable.empty())
        given(mockView.submitButtonClicks).willReturn(Observable.empty())
        given(mockView.forgotPasswordButtonClicks).willReturn(Observable.empty())

        presenter = LogInPresenter(mockLogUserInUseCase, mockRequestNewPasswordUseCase, testDisposables)
    }

    @Test
    fun SetView_Null_DisposablesCleared() {
        presenter.view = mockView

        testDisposables.size().shouldBe(4)

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
        verify(mockView).forgotPasswordButtonClicks
        verify(mockView).emailValidationChanges
        verify(mockView).formValidationChanges
    }

    @Test
    fun SubmitButtonClicks_OnClick_ShowLoadingAndUseCaseExecuted() {
        given(mockView.submitButtonClicks).willReturn(Observable.just(Unit))

        presenter.view = mockView

        verify(mockView, times(1)).showLoading()
        verify(mockLogUserInUseCase).execute(any(), any())
    }

    @Test
    fun ForgotPasswordButtonClicks_OnClick_ShowLoadingAndUseCaseExecuted() {
        given(mockView.forgotPasswordButtonClicks).willReturn(Observable.just(Unit))

        presenter.view = mockView

        verify(mockView, times(1)).showLoading()
        verify(mockRequestNewPasswordUseCase).execute(any(), any())
    }

    @Test
    fun EmailValidationChanges_True_ForgotPasswordButtonEnabled() {
        given(mockView.emailValidationChanges).willReturn(Observable.just(true))

        presenter.view = mockView

        verify(mockView).enableForgotPasswordButton(true)
    }

    @Test
    fun EmailValidationChanges_False_ForgotPasswordButtonDisabled() {
        given(mockView.emailValidationChanges).willReturn(Observable.just(false))

        presenter.view = mockView

        verify(mockView).enableForgotPasswordButton(false)
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
