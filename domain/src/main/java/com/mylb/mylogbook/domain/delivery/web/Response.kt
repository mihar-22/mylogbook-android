package com.mylb.mylogbook.domain.delivery.web

class Response<T> constructor(
        val message: String,
        val data: T?,
        val errors: Map<String, String>?
)
