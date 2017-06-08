package com.mylb.mylogbook.data.cache

import com.chibatching.kotpref.KotprefModel
import com.mylb.mylogbook.domain.cache.UserCache as Cache
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCache @Inject constructor() : KotprefModel(), Cache {
    override var name by nullableStringPref()
    override var email by nullableStringPref()
    override var birthdate by nullableStringPref()
    override var apiToken by nullableStringPref()

    override fun destroy() {
        Timber.d("Destroying")

        name = null
        birthdate = null
        apiToken = null
    }
}
