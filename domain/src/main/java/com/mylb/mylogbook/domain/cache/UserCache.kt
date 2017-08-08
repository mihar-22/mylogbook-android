package com.mylb.mylogbook.domain.cache

import com.mylb.mylogbook.domain.location.Location
import org.joda.time.DateTime

interface UserCache {

    var name: String?
    var email: String?
    var birthdate: DateTime?
    var apiToken: String?
    var lastSyncedAt: DateTime?
    var odometers: HashMap<Int, Int>
    var lastRoute: List<Location>

    fun destroy()

}