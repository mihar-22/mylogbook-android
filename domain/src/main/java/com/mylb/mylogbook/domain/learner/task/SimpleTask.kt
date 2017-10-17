package com.mylb.mylogbook.domain.learner.task

class SimpleTask(
        override val title: String,
        override val isComplete: Boolean,
        override var subtitle: String? = null,
        dependencies: List<BaseTask> = listOf()
) : BaseTask(dependencies) {

    var checkBoxListener: ((Boolean) -> Unit)? = null

}
