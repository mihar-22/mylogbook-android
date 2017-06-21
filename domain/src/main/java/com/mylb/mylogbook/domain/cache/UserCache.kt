package com.mylb.mylogbook.domain.cache

import org.joda.time.DateTime

interface UserCache {

    var name: String?
    var email: String?
    var birthdate: DateTime?
    var apiToken: String?
    var lastSyncedAt: DateTime?

    fun destroy()

}