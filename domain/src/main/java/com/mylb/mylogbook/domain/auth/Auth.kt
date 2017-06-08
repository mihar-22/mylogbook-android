package com.mylb.mylogbook.domain.auth

interface Auth {
    fun setToken(name: String, token: String)
    fun removeAccount(name: String)
    fun <T> processRequest(request: T)
}
