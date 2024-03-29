package com.maxkeppeler.sheets.calendar

import kotlinx.datetime.*

internal fun LocalDate.Companion.now(): LocalDate {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

class LocalDateRange(
    override val start: LocalDate,
    override val endInclusive: LocalDate
) : ClosedRange<LocalDate>, OpenEndRange<LocalDate> {

    override val endExclusive: LocalDate
        get() = endInclusive.plus(1, DateTimeUnit.DAY)

    override fun contains(value: LocalDate): Boolean {
        return value in start..endInclusive
    }

    override fun isEmpty(): Boolean {
        return start > endInclusive
    }

}

internal fun LocalDate.monthShort(): String {
    return when (this.month) {
        Month.JANUARY -> "Jan"
        Month.FEBRUARY -> "Feb"
        Month.MARCH -> "Mar"
        Month.APRIL -> "Apr"
        Month.MAY -> "May"
        Month.JUNE -> "Jun"
        Month.JULY -> "Jul"
        Month.AUGUST -> "Aug"
        Month.SEPTEMBER -> "Sep"
        Month.OCTOBER -> "Oct"
        Month.NOVEMBER -> "Nov"
        Month.DECEMBER -> "Dec"
        else -> month.name
    }
}