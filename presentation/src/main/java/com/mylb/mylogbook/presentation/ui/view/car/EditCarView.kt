package com.mylb.mylogbook.presentation.ui.view.car

import com.mylb.mylogbook.domain.resource.Car
import com.mylb.mylogbook.presentation.validation.Rule
import com.mylb.mylogbook.presentation.validation.ValidatableForm
import com.mylb.mylogbook.presentation.validation.ValidatingView
import com.mylb.mylogbook.presentation.validation.onFormValidationChanges
import io.reactivex.Observable

interface EditCarView<T : Car> : ValidatingView<EditCarView.FormField> {

    val typeSelections: Observable<Int>

    val formValidationChanges: Observable<Boolean>
        get() = onFormValidationChanges(this)

    fun text(field: FormField): String
    fun selectType(position: Int)
    fun enableSubmitButton(isEnabled: Boolean)
    fun finishEditing()

    enum class FormField : ValidatableForm {

        NAME, REGISTRATION, TYPE;

        override fun rules() = when(this) {
            NAME -> listOf(Rule.Required(), Rule.MaxLength(50))
            REGISTRATION -> listOf(Rule.Required(), Rule.MaxLength(6), Rule.AlphaNum())
            TYPE -> null
        }

    }

}
