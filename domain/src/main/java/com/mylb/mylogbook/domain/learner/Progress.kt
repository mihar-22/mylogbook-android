package com.mylb.mylogbook.domain.learner

import com.mylb.mylogbook.domain.cache.ProgressCache
import com.mylb.mylogbook.domain.cache.SettingsCache
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import com.mylb.mylogbook.domain.delivery.local.repository.LocalTripRepository
import com.mylb.mylogbook.domain.learner.task.*
import com.mylb.mylogbook.domain.resource.Supervisor
import com.mylb.mylogbook.domain.resource.Trip
import io.reactivex.rxkotlin.combineLatest
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Years
import javax.inject.Inject

class Progress<S : Supervisor, T : Trip> @Inject constructor(
        private val cache: ProgressCache,
        private val settings: SettingsCache,
        private val user: UserCache,
        private val localSupervisorRepository: LocalSupervisorRepository<S>,
        private val localTripRepository: LocalTripRepository<T>
) {

    val dayBonus: Duration
        get() {
            if (!settings.state.isBonusCreditsAvailable) return Duration.standardSeconds(0)

            var bonus = cache.dayBonus + settings.entryAccreditedDay

            if (settings.state == AustralianState.NEW_SOUTH_WHALES) { bonus += settings.entryAccreditedNight }

            return bonus
        }

    val nightBonus: Duration
        get() {
            if (!settings.state.isBonusCreditsAvailable || settings.state == AustralianState.NEW_SOUTH_WHALES)
                return Duration.standardSeconds(0)

            return cache.nightBonus + settings.entryAccreditedNight
        }

    val totalBonus: Duration
        get() = dayBonus + nightBonus

    val dayLogged: Duration
        get() {
            val logged = cache.dayLogged + settings.entryDay + dayBonus

            if (settings.state == AustralianState.NEW_SOUTH_WHALES) {
                val isAvailable = (logged + nightLogged) >= NewSouthWhalesRequirements.SAFER_DRIVERS_REQUIRED_TIME

                if (isAvailable && cache.isSaferDriversComplete)
                    return logged + NewSouthWhalesRequirements.SAFER_DRIVERS_BONUS
            }

            return logged
        }

    val nightLogged: Duration
        get() = cache.nightLogged + settings.entryNight + nightBonus

    val totalLogged: Duration
        get() = (dayLogged + nightLogged)

    fun refresh() = localTripRepository.all(isAccumulated = true)
            .combineLatest(localSupervisorRepository.all())
            .doOnNext {
                cache.resetTime()
                accumulate(it.first, it.second)
            }

    fun update() = localTripRepository.all(isAccumulated = false)
            .combineLatest(localSupervisorRepository.all())
            .doOnNext { accumulate(it.first, it.second) }

    private fun accumulate(trips: List<T>, supervisors: List<S>) {
        if (trips.count() == 0) return

        accumulateTime(trips, supervisors)
        accumulateOccurrences(trips)

        cache.numberOfTrips += trips.count()
    }

    private fun accumulateTime(trips: List<T>, supervisors: List<S>) {
        val bonusMultiplier = settings.state.bonusMultiplier
        val totalBonusAvailable = settings.state.bonusTimeAvailableWithMultiplier

        var day = Duration.standardSeconds(0)
        var night = Duration.standardSeconds(0)
        var dayBonus = Duration.standardSeconds(0)
        var nightBonus = Duration.standardSeconds(0)

        var bonusRemaining = maxOf(Duration(0), totalBonusAvailable - dayLogged - nightLogged)
        val noDuration = Duration(0)

        for (trip in trips) {
            val loggedTime = trip.calculateLoggedTime()
            val supervisor = supervisors.first { it.id == trip.supervisorId }

            day += loggedTime.day
            night += loggedTime.night

            if (!supervisor.isAccredited || bonusRemaining <= noDuration) continue

            val tripDayBonus = Trip.calculateBonus(loggedTime.day, bonusMultiplier, bonusRemaining)
            bonusRemaining -= tripDayBonus
            val tripNightBonus = Trip.calculateBonus(loggedTime.day, bonusMultiplier, bonusRemaining)
            bonusRemaining -= tripNightBonus

            dayBonus += tripDayBonus
            nightBonus += tripNightBonus
        }

        cache.addTime(day, night, dayBonus, nightBonus)
    }

    private fun accumulateOccurrences(trips: List<T>) {
        val occurrences = cache.conditionOccurrences

        

    }

    val tasks: List<BaseTask>
        get() {
            val state = settings.state
            val age = Years.yearsBetween(DateTime.now(), user.birthdate).years
            val licenseObtainedOn = settings.licenseObtainedOn

            val standardLogTimeTask = LogTimeTask(totalLogged, state.loggedTimeRequired.total)
            val standardHoldLicenseTask = HoldLicenseTask(licenseObtainedOn, state.monthsRequired(age))
            val standardBonusTimeTask = LogTimeTask(totalBonus, state.bonusTimeAvailableWithMultiplier, true)

            fun buildStandardHazardsTask(dependencies: List<BaseTask>): SimpleTask {
                val task = SimpleTask("Hazards Perception Test", cache.isHazardsComplete, null, dependencies)
                task.checkBoxListener = { cache.isHazardsComplete = it }
                return task
            }

            fun buildStandardDrivingTestTask(title: String, dependencies: List<BaseTask>): SimpleTask {
                val task = SimpleTask(title, cache.isDrivingTestComplete, null, dependencies)
                task.checkBoxListener = { cache.isDrivingTestComplete = it }
                return task
            }

            fun buildStandardAssessmentTask(title: String, dependencies: List<BaseTask>): AssessmentTask {
                val task = AssessmentTask(title, cache.assessmentCompletedOn, cache.isAssessmentComplete, dependencies)
                task.checkBoxListener = { cache.isAssessmentComplete = it }
                task.editListener = { cache.assessmentCompletedOn = it }
                return task
            }

            return when (state) {
                AustralianState.VICTORIA -> {
                    val task1 = standardLogTimeTask
                    val task2 = standardHoldLicenseTask
                    val task3 = buildStandardHazardsTask(listOf(task1, task2))
                    val task4 = buildStandardDrivingTestTask("Driving Test", listOf(task3))
                    listOf(task1, task2, task3, task4)
                }

                AustralianState.QUEENSLAND -> {
                    val task1 = standardLogTimeTask
                    val task2 = standardHoldLicenseTask
                    val task3 = standardBonusTimeTask
                    val task4 = buildStandardDrivingTestTask("Practical Driving Test", listOf(task1, task2))
                    listOf(task1, task2, task3, task4)
                }

                AustralianState.TASMANIA -> {
                    val L1 = TasmaniaRequirements.L1
                    val L2 = TasmaniaRequirements.L2

                    val task1 = LogTimeTask(totalLogged, L1.loggedTimeRequired)
                    task1.subtitle = L1.title

                    val task2 = HoldLicenseTask(licenseObtainedOn, L1.monthsRequired)
                    task2.subtitle = L1.title

                    val task3 = buildStandardAssessmentTask("L2 Driving Assessment", listOf(task1, task2))

                    val task4 = LogTimeTask(
                            (totalLogged - L1.loggedTimeRequired), L2.loggedTimeRequired,
                            false,
                            listOf(task3)
                    )
                    task4.subtitle = L2.title

                    val task5 = HoldLicenseTask(cache.assessmentCompletedOn, L2.monthsRequired, listOf(task3))
                    task5.subtitle = L2.title

                    val task6 = buildStandardDrivingTestTask("P1 Driving Assessment", listOf(task4, task5))

                    listOf(task1, task2, task3, task4, task5, task6)
                }

                AustralianState.NEW_SOUTH_WHALES -> {
                    val task1 = standardLogTimeTask
                    val task2 = standardHoldLicenseTask
                    val task3 = standardBonusTimeTask

                    val task4Req = LogTimeTask(totalLogged, NewSouthWhalesRequirements.SAFER_DRIVERS_REQUIRED_TIME)
                    val task4 = SimpleTask("Safer Drivers Course", cache.isSaferDriversComplete, null, listOf(task4Req))
                    task4.subtitle = if (task4.isComplete && task4.isActive) "Earned 20 bonus hours" else {
                        if (task4.isActive) "Earn 20 bonus hours" else "Requires 50 hours logged to unlock"
                    }
                    task4.checkBoxListener = { cache.isSaferDriversComplete = it }

                    val task5 = buildStandardDrivingTestTask("Driving Test", listOf(task1, task2))

                    listOf(task1, task2, task3, task4, task5)
                }

                AustralianState.SOUTH_AUSTRALIA -> {
                    val task1 = standardLogTimeTask
                    val task2 = standardHoldLicenseTask
                    val task3 = buildStandardHazardsTask(listOf(task1, task2))
                    val task4 = buildStandardDrivingTestTask("Vehicle On Road Test", listOf(task3))
                    val task5 = buildStandardDrivingTestTask("Competency Based Training", listOf(task3))
                    listOf(task1, task2 ,task3 ,task4, task5)
                }

                AustralianState.WESTERN_AUSTRALIA -> {
                    val S1 = WesternAustraliaRequirements.S1
                    val S2 = WesternAustraliaRequirements.S2

                    val task1 = LogTimeTask(totalLogged, S1.loggedTimeRequired)
                    task1.subtitle = S1.title

                    val task2 = buildStandardAssessmentTask("Driving Assessment", listOf(task1))

                    val task3 = LogTimeTask((totalLogged - S1.loggedTimeRequired), S2.loggedTimeRequired, false, listOf(task2))
                    task3.subtitle = S2.title

                    val task4 = HoldLicenseTask(cache.assessmentCompletedOn, S2.monthsRequired, listOf(task2))
                    task4.subtitle = S2.title

                    val task5 = buildStandardHazardsTask(listOf(task3, task4))

                    val task6 = buildStandardDrivingTestTask("Get Provisional License", listOf(task5))

                    listOf(task1, task2, task3, task4, task5, task6)
                }
            }
        }
}
