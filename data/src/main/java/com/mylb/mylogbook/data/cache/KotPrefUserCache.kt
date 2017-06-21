package com.mylb.mylogbook.data.cache

import com.chibatching.kotpref.KotprefModel
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.cache.UserCache
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KotPrefUserCache @Inject constructor() : KotprefModel(), UserCache {

    override var name by nullableStringPref()
    override var email by nullableStringPref()
    private var _birthdate by nullableStringPref()
    override var apiToken by nullableStringPref()
    private var _lastSyncedAt by nullableStringPref()

    override var birthdate: DateTime?
        get() = if (_birthdate != null) DateTime.parse(_birthdate, Network.dateFormat) else null
        set(date) { _birthdate = date?.toString() }

    override var lastSyncedAt: DateTime?
        get() = if (_lastSyncedAt != null) DateTime.parse(_lastSyncedAt) else null
        set(date) { _lastSyncedAt = date?.toString() }

    override fun destroy() {
        Timber.d("Destroying")

        name = null
        birthdate = null
        apiToken = null
    }

}
