package com.mylb.mylogbook.presentation.presenter.auth

import com.mylb.mylogbook.domain.interactor.auth.LogUserIn
import com.mylb.mylogbook.domain.interactor.auth.RequestNewPassword
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView.Field.EMAIL
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView.Field.PASSWORD
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
        given(mockView.textChanges(any())).willReturn(Observable.empty())
        given(mockView.submitButtonClicks).willReturn(Observable.empty())
        given(mockView.forgotPasswordButtonClicks).willReturn(Observable.empty())

        presenter = LogInPresenter(mockLogUserInUseCase, mockRequestNewPasswordUseCase, testDisposables)
    }

    @Test
    fun SetView_Null_DisposablesCleared() {
        presenter.view = mockView

        testDisposables.size().shouldBe(3)

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

        verify(mockView).textChanges(EMAIL)
        verify(mockView).textChanges(PASSWORD)
        verify(mockView).submitButtonClicks
        verify(mockView).forgotPasswordButtonClicks
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
    fun OnValidationResult_ValidEmail_ForgotPasswordButtonIsEnabled() {
        given(mockView.textChanges(EMAIL)).willReturn(Observable.just("", "john_doe@mlb.com"))

        presenter.view = mockView

        verify(mockView).enableForgotPasswordButton(true)
    }

    @Test
    fun OnValidationResult_InvalidEmail_ForgotPasswordButtonIsDisabled() {
        given(mockView.textChanges(EMAIL)).willReturn(Observable.just("", "invalid-email"))

        presenter.view = mockView

        verify(mockView).enableForgotPasswordButton(false)
    }

}
