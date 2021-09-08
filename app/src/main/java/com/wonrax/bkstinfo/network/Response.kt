package com.wonrax.bkstinfo.network

import okhttp3.Headers

data class Response(val body: String, val headers: Headers, val code: Int)
