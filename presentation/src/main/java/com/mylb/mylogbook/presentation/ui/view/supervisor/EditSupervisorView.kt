package com.mylb.mylogbook.presentation.ui.view.supervisor

import com.mylb.mylogbook.domain.resource.Supervisor
import com.mylb.mylogbook.presentation.validation.Rule
import com.mylb.mylogbook.presentation.validation.ValidatableForm
import com.mylb.mylogbook.presentation.validation.ValidatingView
import com.mylb.mylogbook.presentation.validation.onFormValidationChanges
import io.reactivex.Observable

interface EditSupervisorView<T : Supervisor> : ValidatingView<EditSupervisorView.FormField> {

    val maleCheckedChanges: Observable<Boolean>
    val femaleCheckedChanges: Observable<Boolean>
    val accreditedCheckedChanges: Observable<Boolean>

    val formValidationChanges: Observable<Boolean>
        get() = onFormValidationChanges(this)

    fun value(field: FormField): Any
    fun selectGender(isMale: Boolean)
    fun checkIsAccredited(isChecked: Boolean)
    fun enableSubmitButton(isEnabled: Boolean)
    fun finishEditing()

    enum class FormField : ValidatableForm {

        NAME, GENDER, IS_ACCREDITED;

        override fun rules() = when(this) {
            NAME -> listOf(Rule.Required(), Rule.MaxLength(100))
            GENDER -> null
            IS_ACCREDITED -> null
        }

    }

}

