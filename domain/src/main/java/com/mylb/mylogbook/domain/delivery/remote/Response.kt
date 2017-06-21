package com.mylb.mylogbook.domain.delivery.remote

class Response<T> constructor(
        val message: String,
        val data: T?,
        val errors: Map<String, String>?
)
