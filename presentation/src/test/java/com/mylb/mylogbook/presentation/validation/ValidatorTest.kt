package com.mylb.mylogbook.presentation.validation

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ValidatorTest {

    lateinit private var testObserver: TestObserver<Any>

    @Mock lateinit private var mockView: ValidatingView<TestForm>

    @Before
    fun setUp() { testObserver = TestObserver() }

    @Test
    fun ValidationChanges_TextInputStream_ValidationsEmit() {
        // Given
        given(mockView.textChanges(TestForm.FIELD1)).willReturn(Observable.just("", "a", "ab", "abc"))
        given(mockView.textChanges(TestForm.FIELD2)).willReturn(Observable.just("", "a", "ab", "abc"))

        // When
        Validator.validationChanges(mockView, this::rules).subscribe(testObserver)

        // Then
        testObserver.assertValueCount(3)
                .assertValues(false, false, true)
                .assertNoErrors()
                .assertComplete()
    }

    @Test
    fun ValidationChanges_TextInputStream_ShowErrorOnView() {
        // Given
        given(mockView.textChanges(TestForm.FIELD1)).willReturn(Observable.just("", "a", "ab", "abc"))
        given(mockView.textChanges(TestForm.FIELD2)).willReturn(Observable.just("", "a", "ab", "abc"))

        // When
        Validator.validationChanges(mockView, this::rules).subscribe(testObserver)

        // Then
        verify(mockView, never()).showError(TestForm.FIELD1, rules[0].errorMessage)
        verify(mockView, times(1)).showError(TestForm.FIELD1, null)
        verify(mockView, times(2)).showError(TestForm.FIELD1, rules[1].errorMessage)
    }

    @Test
    fun ValidationChanges_MultipleTextInputStreams_ValidationsCombinedAndEmit() {
        // Given
        given(mockView.textChanges(TestForm.FIELD1)).willReturn(Observable.just("", "a", "ab", "abc"))
        given(mockView.textChanges(TestForm.FIELD2)).willReturn(Observable.just("", "", "", "", "abc"))

        // When
        Validator.validationChanges(mockView, this::rules).subscribe(testObserver)

        // Then
        testObserver.assertValueCount(4)
                .assertValues(false, false, false, true)
                .assertNoErrors()
                .assertComplete()
    }

    private enum class TestForm { FIELD1, FIELD2 }

    private val rules = arrayListOf<ValidationRule>(Validator.Required(), Validator.MinLength(3))

    private fun rules(field: TestForm): ArrayList<ValidationRule> = rules
}