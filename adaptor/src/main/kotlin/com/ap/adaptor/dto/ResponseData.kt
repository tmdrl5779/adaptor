package com.ap.adaptor.dto

data class ResponseData(
    var responseTime: Long = 0,
    var body: Any ?= null,
    var status: String = ""
)
