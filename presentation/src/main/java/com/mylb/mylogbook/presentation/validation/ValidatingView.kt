package com.mylb.mylogbook.presentation.validation

import io.reactivex.Observable

interface ValidatingView<T : Enum<T>> {
    fun textChanges(field: T): Observable<CharSequence>
    fun showError(field: T, error: CharSequence?)
}