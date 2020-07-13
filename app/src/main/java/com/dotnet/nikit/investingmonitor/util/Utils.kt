package com.dotnet.nikit.investingmonitor.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Utils {
    fun formatDateToString(date: Date?): String? {
        if(date == null) return null
        return SimpleDateFormat("dd/M/yyyy").format(date)
    }

    fun formatStringToDate(date: String?): Date? {
        if(date == null) return null
        return SimpleDateFormat("dd/M/yyyy").parse(date)
    }
}