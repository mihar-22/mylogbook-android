package com.mylb.mylogbook.domain.test.rule

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.TimeUnit

class RxTestScheduler : TestRule {

    private var scheduler = TestScheduler()

    val delay: Long = 50
    val delayTimeUnit = TimeUnit.MILLISECONDS

    val main: TestScheduler
        get() = scheduler

    fun advanceTime() = scheduler.advanceTimeBy(delay, delayTimeUnit)

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { scheduler }
                RxJavaPlugins.setComputationSchedulerHandler { scheduler }
                RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }

                try { base?.evaluate() } finally { RxJavaPlugins.reset() }
            }
        }
    }

}
