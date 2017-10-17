package com.mylb.mylogbook.domain.learner.task

abstract class BaseTask(protected val dependencies: List<BaseTask> = listOf()) {

    abstract val title: String
    abstract val subtitle: String?
    abstract val isComplete: Boolean

    val isActive: Boolean
        get() = (dependencies.isEmpty() || dependencies.any { !it.isActive && !it.isComplete })

}

