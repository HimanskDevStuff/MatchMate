package com.match.matchmate.core.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun extractYearFromIsoAndroid(timestamp: String): Int {
    return try {
        // ISO 8601 format
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(timestamp)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        calendar.get(Calendar.YEAR)
    } catch (e: ParseException) {
        // Handle invalid format
        -1
    }
}