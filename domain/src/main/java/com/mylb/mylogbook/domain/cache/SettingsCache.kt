package com.mylb.mylogbook.domain.cache

import com.mylb.mylogbook.domain.learner.AustralianState
import org.joda.time.DateTime
import org.joda.time.Duration

interface SettingsCache {

    var licenseObtainedOn: DateTime
    var state: AustralianState
    var entryDay: Duration
    var entryNight: Duration
    var entryAccreditedDay: Duration
    var entryAccreditedNight: Duration
    var isSetup: Boolean

}
