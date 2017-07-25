package com.mylb.mylogbook.presentation.ui.fragment.car

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.widget.itemSelections
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.domain.resource.car.CarBody
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.presenter.car.EditCarPresenter
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import com.mylb.mylogbook.presentation.ui.fragment.util.FragmentCompanion
import com.mylb.mylogbook.presentation.ui.view.car.EditCarView
import com.mylb.mylogbook.presentation.ui.view.car.EditCarView.FormField.*
import com.mylb.mylogbook.presentation.util.carDrawable
import com.mylb.mylogbook.presentation.util.enable
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_edit_car.*
import me.eugeniomarletti.extras.intent.IntentExtra
import me.eugeniomarletti.extras.intent.base.Parcelable
import timber.log.Timber
import javax.inject.Inject

class EditCarFragment : BaseFragment(), EditCarView<Car> {

    @Inject override lateinit var presenter: EditCarPresenter<Car>

    private var editCar: Car? = null
    private var saveMenuItem: MenuItem? = null
    private val carBodies = CarBody.values()

    override val typeSelections: Observable<Int>
        get() = typeSpinner.itemSelections()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        component.inject(this)

        hideBottomNavigation()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        activity.title = getString(R.string.car_new_title)

        return inflater.inflate(R.layout.fragment_edit_car, container, false)!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTypeSpinner()
    }

    private fun setupTypeSpinner() {
        val adapter = ArrayAdapter<String>(
                activity,
                R.layout.spinner_item,
                carBodies.map { it.displayName }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        typeSpinner.adapter = adapter
    }

    private fun unpackIntent() {
        intent.options { editCar = it.car }

        if (editCar != null) {
            activity.title = getString(R.string.car_edit_title)

            nameEditText.setText(editCar!!.name)
            registrationEditText.setText(editCar!!.registration)
            typeImageView.setImageDrawable(activity.carDrawable(editCar!!.body))
            typeSpinner.setSelection(editCar!!.body.ordinal)
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
        presenter.onSubmitButtonClick(editCar ?: Car())

        return true
    }

    override fun enableSubmitButton(isEnabled: Boolean) { saveMenuItem?.enable(isEnabled) }

    override fun finishEditing() {
        Timber.d("Finished editing")

        popOffBackStack()
    }

    override fun text(field: EditCarView.FormField) = when(field) {
        NAME -> nameEditText.text.trim().toString()
        REGISTRATION -> registrationEditText.text.trim().toString()
        TYPE -> carBodies[typeSpinner.selectedItemPosition].type
    }

    override fun textInputLayout(field: EditCarView.FormField) = when(field) {
        NAME -> nameTextInputLayout
        REGISTRATION -> registrationTextInputLayout
        TYPE -> null
    }

    override fun selectType(position: Int) {
        val body = carBodies[position]

        typeSpinner.setSelection(position)
        typeImageView.setImageDrawable(activity.carDrawable(body))
    }

    companion object Builder : FragmentCompanion<IntentOptions>(IntentOptions, EditCarFragment::class) {

        fun build() = EditCarFragment()

    }

    object IntentOptions {
        var Intent.car by IntentExtra.Parcelable<Car>()
    }

}
