package com.mylb.mylogbook.domain.learner.task

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class AssessmentTask(
        override val title: String,
        private val completedOn: DateTime?,
        override val isComplete: Boolean,
        dependencies: List<BaseTask> = listOf()
) : BaseTask(dependencies) {

    override val subtitle: String?
        get() = completedOn?.toString(DateTimeFormat.forPattern("dd-MM-yyyy"))

    var checkBoxListener: ((Boolean) -> Unit)? = null
    var editListener: ((DateTime) -> Unit)? = null

}
