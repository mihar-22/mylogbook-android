package com.mylb.mylogbook.domain.learner.task

import org.joda.time.Duration

class LogTimeTask(
        val completed: Duration,
        val required: Duration,
        val isBonusTime: Boolean = false,
        dependencies: List<BaseTask> = listOf()
) : BaseTask(dependencies) {

    override val title: String
        get() {
            val prefix = if (isBonusTime) "Earned" else "Total of"
            val postfix = if (isBonusTime) "bonus hours " else "hours logged"

            return "$prefix ${completed.toStandardHours()} / ${ required.toStandardHours()} $postfix"
        }

    override var subtitle: String? = null

    override val isComplete: Boolean
        get() = (completed >= required)

}
