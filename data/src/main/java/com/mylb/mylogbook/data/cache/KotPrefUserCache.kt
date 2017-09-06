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

class KotPrefUserCache constructor(context: Context) : KotprefModel(), UserCache {

    override val kotprefName = "${context.packageName}_user"
    override val kotprefMode = Context.MODE_MULTI_PROCESS

    override var name by nullableStringPref()
    override var email by nullableStringPref()
    private var _birthdate by nullableStringPref()
    override var apiToken by nullableStringPref()
    private var _odometers by nullableStringPref()
    private var _lastSyncedAt by nullableStringPref()
    private var _lastRoute by nullableStringPref()

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

    override fun destroy() {
        Timber.d("Destroying")

        name = null
        birthdate = null
        apiToken = null
    }

}
