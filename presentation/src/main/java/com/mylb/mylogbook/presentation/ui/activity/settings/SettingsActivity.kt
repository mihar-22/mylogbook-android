package com.mylb.mylogbook.presentation.ui.activity.settings

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.view.View
import android.widget.DatePicker
import com.mylb.mylogbook.domain.cache.SettingsCache
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.interactor.auth.LogUserOut
import com.mylb.mylogbook.domain.learner.AustralianState
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.SystemIntent
import com.mylb.mylogbook.presentation.di.component.AuthComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAuthComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity
import com.mylb.mylogbook.presentation.ui.activity.LaunchActivity
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_settings.*
import me.eugeniomarletti.extras.SimpleActivityCompanion
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.PeriodFormatterBuilder
import timber.log.Timber
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    override val presenter: Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        fragmentManager.beginTransaction()
                .replace(frameLayout.id, SettingsFragment.build())
                .commit()
    }

    fun loadDocument(document: SettingsFragment.LegalDocument) {
        title = when (document) {
            SettingsFragment.LegalDocument.PRIVACY_POLICY ->
                getString(R.string.settings_legal_privacy_policy_title)

            SettingsFragment.LegalDocument.TERMS_OF_SERVICE ->
                getString(R.string.settings_legal_terms_of_service_title)
        }

        webView.loadUrl(document.localUrl)
        webView.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (webView.visibility == View.VISIBLE) {
            title = getString(R.string.settings)
            webView.visibility = View.GONE
            return
        }

        super.onBackPressed()
    }

    companion object Builder : SimpleActivityCompanion(SettingsActivity::class) {

        fun build() = SettingsActivity()

    }

    class SettingsFragment : PreferenceFragment(), DatePickerDialog.OnDateSetListener {

        @Inject lateinit var userCache: UserCache
        @Inject lateinit var settings: SettingsCache
        @Inject lateinit var logUserOut: LogUserOut

        private val dateFormat = DateTimeFormat.forPattern("dd-MM-yyyy")

        private val settingsActivity: SettingsActivity
            get() = (activity as SettingsActivity)

        private val component: AuthComponent
            get() = DaggerAuthComponent.builder()
                    .applicationComponent(settingsActivity.applicationComponent)
                    .androidModule(settingsActivity.activityModule)
                    .build()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)

            component.inject(this)

            setupSummaries()
            setupListeners()
            enableAccreditedTimeCategory()
        }

        private fun setupSummaries() {
            setSummary(SettingsOption.EMAIL, userCache.email!!)
            setSummary(SettingsOption.LICENSE_OBTAINED, settings.licenseObtainedOn.toString(dateFormat))
            setSummary(SettingsOption.ENTRY_DAY, formatTime(settings.entryDay))
            setSummary(SettingsOption.ENTRY_NIGHT, formatTime(settings.entryNight))
            setSummary(SettingsOption.ENTRY_ACCREDITED_DAY, formatTime(settings.entryAccreditedDay))
            setSummary(SettingsOption.ENTRY_ACCREDITED_NIGHT, formatTime(settings.entryAccreditedNight))
        }

        private fun setupListeners() {
            preference(SettingsOption.LICENSE_OBTAINED).setOnPreferenceClickListener {
                showDatePickerDialog()
                true
            }

            preference(SettingsOption.STATE).setOnPreferenceChangeListener { _, newValue ->
                val state = AustralianState.values().first { it.displayName == newValue.toString() }
                settings.state = state
                resetEntries()
                setupSummaries()
                enableAccreditedTimeCategory()
                true
            }

            preference(SettingsOption.ENTRY_DAY).setOnPreferenceChangeListener { _, newValue ->
                setEntry(SettingsOption.ENTRY_DAY, newValue)
                true
            }

            preference(SettingsOption.ENTRY_NIGHT).setOnPreferenceChangeListener { _, newValue ->
                setEntry(SettingsOption.ENTRY_NIGHT, newValue)
                true
            }

            preference(SettingsOption.ENTRY_ACCREDITED_DAY).setOnPreferenceChangeListener { _, newValue ->
                setEntry(SettingsOption.ENTRY_ACCREDITED_DAY, newValue)
                true
            }

            preference(SettingsOption.ENTRY_ACCREDITED_NIGHT).setOnPreferenceChangeListener { _, newValue ->
                setEntry(SettingsOption.ENTRY_ACCREDITED_NIGHT, newValue)
                true
            }

            preference(SettingsOption.LEGAL).setOnPreferenceClickListener {
                showLegalAlertDialog()
                true
            }

            preference(SettingsOption.FEEDBACK).setOnPreferenceClickListener {
                showFeedbackAlertDialog()
                true
            }

            preference(SettingsOption.LOGOUT).setOnPreferenceClickListener {
                logout()
                true
            }
        }

        private fun enableAccreditedTimeCategory() {
            val category = preference(SettingsOption.ENTRY_CATEGORY_ACCREDITED_TIME) as PreferenceCategory
            category.isEnabled = settings.state.isBonusCreditsAvailable
        }

        private fun setEntry(option: SettingsOption, value: Any) {
            val state = settings.state
            val minutes = value.toString().toLong()
            val duration = Duration.standardMinutes(minutes)
            var adjustedDuration = Duration.standardSeconds(0)

            when(option) {
                SettingsOption.ENTRY_DAY -> {
                    val maxDuration = state.loggedTimeRequired.day

                    adjustedDuration = if (state.loggedTimeRequired.night != null) {
                        minOf(duration, maxDuration)
                    } else {
                        val totalDurationRemaining = maxDuration.minus(settings.entryNight)
                        minOf(duration, totalDurationRemaining)
                    }

                    settings.entryDay = adjustedDuration
                }

                SettingsOption.ENTRY_NIGHT -> {
                    adjustedDuration = if (state.loggedTimeRequired.night != null) {
                        val maxDuration = state.loggedTimeRequired.night!!
                        minOf(duration, maxDuration)
                    } else {
                        val maxDuration = state.loggedTimeRequired.total
                        val totalDurationRemaining = maxDuration.minus(settings.entryDay)
                        minOf(duration, totalDurationRemaining)
                    }

                    settings.entryNight = adjustedDuration
                }

                SettingsOption.ENTRY_ACCREDITED_DAY -> {
                    val maxDuration = state.bonusTimeAvailable
                    val totalDurationRemaining = maxDuration.minus(settings.entryAccreditedNight)
                    adjustedDuration = minOf(duration, totalDurationRemaining)
                    settings.entryAccreditedDay = adjustedDuration.multipliedBy(state.bonusMultiplier.toLong())
                }

                SettingsOption.ENTRY_ACCREDITED_NIGHT -> {
                    val maxDuration = state.bonusTimeAvailable
                    val totalDurationRemaining = maxDuration.minus(settings.entryAccreditedDay)
                    adjustedDuration = minOf(duration, totalDurationRemaining)
                    settings.entryAccreditedNight = adjustedDuration.multipliedBy(state.bonusMultiplier.toLong())
                }

                else -> return
            }

            setSummary(option, formatTime(adjustedDuration))
        }

        private fun resetEntries() {
            val noDuration = Duration.standardSeconds(0)

            settings.entryDay = noDuration
            settings.entryNight = noDuration
            settings.entryAccreditedDay = noDuration
            settings.entryAccreditedNight = noDuration
        }

        private fun showDatePickerDialog() {
            val date = settings.licenseObtainedOn

            val dialog = DatePickerDialog(activity, this, date.year, date.monthOfYear - 1, date.dayOfMonth)
            dialog.datePicker.maxDate = DateTime.now().millis
            dialog.show()
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
            val date = DateTime.parse("$dayOfMonth-${month+1}-$year", dateFormat)

            settings.licenseObtainedOn = date

            setSummary(SettingsOption.LICENSE_OBTAINED, date.toString(dateFormat))
        }

        private fun formatTime(duration: Duration): String {
            val formatter = PeriodFormatterBuilder()
                    .appendHours()
                    .appendSuffix("h")
                    .appendSeparator(" ")
                    .appendMinutes()
                    .appendSuffix("m")
                    .appendSeparator(" ")
                    .appendSeconds()
                    .appendSuffix("s")
                    .toFormatter()

            return formatter.print(duration.toPeriod())
        }

        private fun showLegalAlertDialog() {
            val items = arrayOf(
                    getString(R.string.settings_legal_privacy_policy_title),
                    getString(R.string.settings_legal_terms_of_service_title)
            )

            AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.settings_legal_title))
                    .setItems(items, { _, which ->
                        var document: LegalDocument? = null

                        when (which) {
                            0 -> document = LegalDocument.PRIVACY_POLICY
                            1 -> document = LegalDocument.TERMS_OF_SERVICE
                        }

                        settingsActivity.loadDocument(document!!)
                    })
                    .show()
        }

        private fun showFeedbackAlertDialog() {
            val items = arrayOf(
                    getString(R.string.settings_feedback_general_title),
                    getString(R.string.settings_feedback_bug_title),
                    getString(R.string.settings_feedback_help_title)
            )

            AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.settings_feedback_title))
                    .setItems(items, { _, which ->
                        var category: String? = null

                        when (which) {
                            0 -> category = "feedback"
                            1 -> category = "bug"
                            2 -> category = "help"
                        }

                        SystemIntent.openMail(
                                activity,
                                "support+$category@mylb.com.au",
                                category!!.capitalize()
                        )
                    })
                    .show()
        }

        private fun logout() {
            logUserOut.execute(LogUserOutObserver(), Unit)
            LaunchActivity.start(activity)
        }

        private fun preference(option: SettingsOption) = preferenceManager.findPreference(option.key)!!

        private fun setSummary(option: SettingsOption, value: String) { preference(option).summary = value }

        private inner class LogUserOutObserver : DisposableObserver<Response<Unit>>() {

            override fun onError(e: Throwable) {
                Timber.d("Logging out out failed with: %s", e.message)

                activity.finish()
            }

            override fun onComplete() = Unit

            override fun onNext(t: Response<Unit>) { activity.finish() }

        }

        private enum class SettingsOption(val key: String) {
            EMAIL("email"),
            LICENSE_OBTAINED("license_obtained_on"),
            STATE("state"),
            ENTRY_DAY("entry_day"),
            ENTRY_NIGHT("entry_night"),
            ENTRY_ACCREDITED_DAY("entry_accredited_day"),
            ENTRY_ACCREDITED_NIGHT("entry_accredited_night"),
            LEGAL("legal"),
            FEEDBACK("feedback"),
            LOGOUT("logout"),

            ENTRY_CATEGORY_TIME("entry_category_time"),
            ENTRY_CATEGORY_ACCREDITED_TIME("entry_category_accredited_time");
        }

        enum class LegalDocument(val localUrl: String) {
            PRIVACY_POLICY("file:///android_asset/legal/privacy-policy.html"),
            TERMS_OF_SERVICE("file:///android_asset/legal/terms-of-service.html")
        }

        companion object Builder {

            fun build() = SettingsFragment()

        }

    }

}
