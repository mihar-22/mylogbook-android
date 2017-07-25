package com.mylb.mylogbook.presentation.ui.fragment.supervisor

import android.content.Intent
import android.os.Bundle
import android.view.*
import com.jakewharton.rxbinding2.widget.checkedChanges
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.domain.resource.supervisor.SupervisorGender
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.presenter.supervisor.EditSupervisorPresenter
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import com.mylb.mylogbook.presentation.ui.fragment.util.FragmentCompanion
import com.mylb.mylogbook.presentation.ui.view.supervisor.EditSupervisorView
import com.mylb.mylogbook.presentation.ui.view.supervisor.EditSupervisorView.FormField
import com.mylb.mylogbook.presentation.ui.view.supervisor.EditSupervisorView.FormField.*
import com.mylb.mylogbook.presentation.util.enable
import com.mylb.mylogbook.presentation.util.supervisorDrawable
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_edit_supervisor.*
import me.eugeniomarletti.extras.intent.IntentExtra
import me.eugeniomarletti.extras.intent.base.Parcelable
import timber.log.Timber
import javax.inject.Inject

class EditSupervisorFragment : BaseFragment(), EditSupervisorView<Supervisor> {

    @Inject override lateinit var presenter: EditSupervisorPresenter<Supervisor>

    private var editSupervisor: Supervisor? = null
    private var saveMenuItem: MenuItem? = null

    override val accreditedCheckedChanges: Observable<Boolean>
        get() = accreditedSwitch.checkedChanges().skip(1)

    override val maleCheckedChanges: Observable<Boolean>
        get() = genderMaleRadioButton.checkedChanges().skip(1)

    override val femaleCheckedChanges: Observable<Boolean>
        get() = genderFemaleRadioButton.checkedChanges().skip(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        component.inject(this)

        hideBottomNavigation()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        activity.title = getString(R.string.supervisor_new_title)

        return inflater.inflate(R.layout.fragment_edit_supervisor, container, false)!!
    }

    private fun unpackIntent() {
        intent.options { editSupervisor = it.supervisor }

        if (editSupervisor != null) {
            activity.title = getString(R.string.supervisor_edit_title)

            nameEditText.setText(editSupervisor!!.name)
            genderMaleRadioButton.isChecked = (editSupervisor!!.fullGender == SupervisorGender.MALE)
            genderFemaleRadioButton.isChecked = (editSupervisor!!.fullGender == SupervisorGender.FEMALE)
            accreditedSwitch.isChecked = editSupervisor!!.isAccredited

            avatarImageView.setImageDrawable(activity.supervisorDrawable(
                    editSupervisor!!.fullGender,
                    editSupervisor!!.isAccredited
            ))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)

        saveMenuItem = menu.findItem(R.id.saveMenuItem)
        saveMenuItem!!.enable(false)

        presenter.view = this

        unpackIntent()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        hideSoftKeyboard()
        presenter.onSubmitButtonClick(editSupervisor ?: Supervisor())

        return true
    }

    override fun value(field: FormField): Any = when(field) {
        NAME -> nameEditText.text.trim().toString()
        GENDER -> getGender().code
        IS_ACCREDITED -> accreditedSwitch.isChecked
    }

    override fun selectGender(isMale: Boolean) {
        Timber.d("Selected gender %s", if (isMale) "Male" else "Female")

        genderMaleRadioButton.isChecked = isMale
        genderFemaleRadioButton.isChecked = !isMale

        avatarImageView.setImageDrawable(activity.supervisorDrawable(
                getGender(),
                accreditedSwitch.isChecked
        ))
    }

    override fun checkIsAccredited(isChecked: Boolean) {
        Timber.d("Checked is accredited: %s", isChecked)

        accreditedSwitch.isChecked = isChecked

        avatarImageView.setImageDrawable(activity.supervisorDrawable(getGender(), isChecked))
    }

    private fun getGender() =
            if (genderMaleRadioButton.isChecked) SupervisorGender.MALE else SupervisorGender.FEMALE

    override fun textInputLayout(field: EditSupervisorView.FormField) = when(field) {
        NAME -> nameTextInputLayout
        else -> null
    }

    override fun enableSubmitButton(isEnabled: Boolean) { saveMenuItem?.enable(isEnabled) }

    override fun finishEditing() {
        Timber.d("Finished editing")

        popOffBackStack()
    }

    companion object Builder : FragmentCompanion<IntentOptions>(IntentOptions, EditSupervisorFragment::class) {

        fun build() = EditSupervisorFragment()

    }

    object IntentOptions {
        var Intent.supervisor by IntentExtra.Parcelable<Supervisor>()
    }


}