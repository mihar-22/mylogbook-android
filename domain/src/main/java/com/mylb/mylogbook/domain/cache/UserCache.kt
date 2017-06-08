package com.mylb.mylogbook.domain.cache

interface UserCache {
    var name: String?
    var email: String?
    var birthdate: String?
    var apiToken: String?

    fun destroy()
}