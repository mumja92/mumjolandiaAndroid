package com.mumjolandia.android.utils

import java.text.SimpleDateFormat
import java.util.*

class DateHelper {
    private val datePattern = "HH:mm"
    fun getDateFromString(dateString: String, shiftMinutes: Int = 0): Date?{
        val date = SimpleDateFormat(datePattern, Locale.getDefault()).parse(dateString) ?: return null
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(
            Calendar.MINUTE,
            shiftMinutes
        )
        return calendar.time
    }

    fun getCalendarFromString(dateString: String, shiftMinutes: Int = 0): Calendar?{
        val date = SimpleDateFormat(datePattern, Locale.getDefault()).parse(dateString) ?: return null
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(
            Calendar.MINUTE,
            shiftMinutes
        )
        return calendar

    }

    fun getStringFromDate(date: Date): String{
        val c = Calendar.getInstance()
        c.time = date
        return SimpleDateFormat(datePattern, Locale.getDefault()).format(c.time)
    }
}