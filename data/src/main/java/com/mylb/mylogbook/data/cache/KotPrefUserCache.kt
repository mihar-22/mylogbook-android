package com.mylb.mylogbook.data.cache

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import com.google.gson.reflect.TypeToken
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.learner.AustralianState
import com.mylb.mylogbook.domain.location.Location
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KotPrefUserCache @Inject constructor() : KotprefModel(), UserCache {

    override val kotprefMode = Context.MODE_MULTI_PROCESS

    override var name by nullableStringPref()
    override var email by nullableStringPref()
    private var _birthdate by nullableStringPref()
    override var apiToken by nullableStringPref()
    private var _odometers by nullableStringPref()
    private var _lastSyncedAt by nullableStringPref()
    private var _lastRoute by nullableStringPref()
    private var _receivedLicenseDate by nullableStringPref()
    private var _state by nullableStringPref()

    override var birthdate: DateTime?
        get() = if (_birthdate != null) DateTime.parse(_birthdate, Network.dateFormat) else null
        set(date) { _birthdate = date?.toString(Network.dateFormat) }

    override var lastSyncedAt: DateTime?
        get() = if (_lastSyncedAt != null) DateTime.parse(_lastSyncedAt) else null
        set(date) { _lastSyncedAt = date?.toString() }

    override var odometers: HashMap<Int, Int>
        get() = if (_odometers != null) {
            Network.converter.fromJson(_odometers, object : TypeToken<HashMap<Int, Int>>() {}.type)
        } else HashMap()
        set(odometers) { _odometers = Network.converter.toJson(odometers) }

    override var lastRoute: List<Location>
        get() = if (_lastRoute != null) {
            Network.converter.fromJson(_lastRoute, object : TypeToken<List<Location>>() {}.type)
        } else ArrayList()
        set(lastRoute) { _lastRoute = Network.converter.toJson(lastRoute) }

    override var receivedLicenseDate: DateTime?
        get() = if (_receivedLicenseDate != null) DateTime.parse(_receivedLicenseDate, Network.dateFormat) else null
        set(date) { _receivedLicenseDate = date?.toString(Network.dateFormat) }

    override var state: AustralianState?
        get() = if (_state != null) AustralianState.values().first { it.abbreviation == _state } else null
        set(state) { _state = state?.abbreviation }

    override fun destroy() {
        Timber.d("Destroying")

        name = null
        birthdate = null
        apiToken = null
    }

}
