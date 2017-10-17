package com.mylb.mylogbook.domain.learner.task

import org.joda.time.DateTime
import org.joda.time.Months

class HoldLicenseTask(
        private val licenseObtainedOn: DateTime,
        private val monthsRequired: Int,
        dependencies: List<BaseTask> = listOf()
) : BaseTask(dependencies) {

    override val title: String
        get() = "License held for $monthsHeld / $monthsRequired months"

    override var subtitle: String? = null

    override val isComplete: Boolean
        get() = (monthsHeld >= monthsRequired)

    private val monthsHeld: Int
        get() = Months.monthsBetween(DateTime.now(), licenseObtainedOn).months

}
