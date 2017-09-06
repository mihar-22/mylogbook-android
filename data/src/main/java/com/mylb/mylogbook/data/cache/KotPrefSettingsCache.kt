package com.mylb.mylogbook.data.cache

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.cache.SettingsCache
import com.mylb.mylogbook.domain.learner.AustralianState
import org.joda.time.DateTime
import org.joda.time.Duration

class KotPrefSettingsCache constructor(context: Context) : KotprefModel(), SettingsCache {

    override val kotprefName = "${context.packageName}_preferences"
    override val kotprefMode = Context.MODE_PRIVATE

    private var _licenseObtainedOn by stringPref(default = DateTime.now().toString(Network.dateFormat))
    private var _state by stringPref(key = "state", default = AustralianState.VICTORIA.displayName)
    private var _entryDay by longPref(default = 0)
    private var _entryNight by longPref(default = 0)
    private var _entryAccreditedDay by longPref(default = 0)
    private var _entryAccreditedNight by longPref(default = 0)
    override var isSetup by booleanPref(default = false)

    override var entryDay: Duration
        get() = Duration.millis(_entryDay)
        set(duration) { _entryDay = duration.millis }

    override var entryNight: Duration
        get() = Duration.millis(_entryNight)
        set(duration) { _entryNight = duration.millis }

    override var entryAccreditedDay: Duration
        get() = Duration.millis(_entryAccreditedDay)
        set(duration) { _entryAccreditedDay = duration.millis }

    override var entryAccreditedNight: Duration
        get() = Duration.millis(_entryAccreditedNight)
        set(duration) { _entryAccreditedNight = duration.millis }

    override var licenseObtainedOn: DateTime
        get() = DateTime.parse(_licenseObtainedOn, Network.dateFormat)
        set(date) { _licenseObtainedOn = date.toString(Network.dateFormat) }

    override var state: AustralianState
        get() = AustralianState.values().first { it.displayName == _state }
        set(state) { _state = state.displayName }

}