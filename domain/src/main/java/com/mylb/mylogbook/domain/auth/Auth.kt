package com.mylb.mylogbook.domain.auth

interface Auth {

    fun setToken(name: String, token: String)
    fun removeAccount(name: String)
    fun accountExists(): Boolean
    fun isAuthenticated(): Boolean
    fun <T> processRequest(request: T)
    fun <T> peekToken(account: T): String?

}
