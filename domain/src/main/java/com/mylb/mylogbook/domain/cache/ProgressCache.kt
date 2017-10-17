package com.mylb.mylogbook.domain.cache

import org.joda.time.DateTime
import org.joda.time.Duration

interface ProgressCache {

    var dayLogged: Duration
    var nightLogged: Duration
    var dayBonus: Duration
    var nightBonus: Duration
    var numberOfTrips: Int
    var isHazardsComplete: Boolean
    var isDrivingTestComplete: Boolean
    var isSaferDriversComplete: Boolean
    var isAssessmentComplete: Boolean
    var assessmentCompletedOn: DateTime
    var conditionOccurrences: HashMap<String, Int>

    fun resetTime()
    fun addTime(day: Duration, night: Duration, dayBonus: Duration, nightBonus: Duration)

}
