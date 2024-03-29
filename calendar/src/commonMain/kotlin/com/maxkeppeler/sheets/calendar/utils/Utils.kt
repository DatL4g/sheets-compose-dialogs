/*
 *  Copyright (C) 2022-2024. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.maxkeppeler.sheets.calendar.utils

import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarData
import com.maxkeppeler.sheets.calendar.models.CalendarDateData
import com.maxkeppeler.sheets.calendar.models.CalendarMonthData
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.maxkeppeler.sheets.calendar.models.CalendarViewType
import com.maxkeppeler.sheets.calendar.now
import kotlinx.datetime.*

/**
 * Returns the date for the first day of the week (Monday) for this [LocalDate].
 */
internal val LocalDate.startOfWeek: LocalDate
    get() = minus(dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)

/**
 * Returns the date for the last day of the week (Sunday) for this [LocalDate].
 */
internal val LocalDate.endOfWeek: LocalDate
    get() = plus(7 - dayOfWeek.isoDayNumber, DateTimeUnit.DAY)

/**
 * Extension function that jumps to the first day of the same week (Monday) or the first day of the month, whichever comes first.
 *
 * @return [LocalDate] representing the first day of the week or the month, whichever is earliest
 */
internal val LocalDate.startOfWeekOrMonth: LocalDate
    get() {
        var result = this
        while (result.dayOfMonth > 1 && result.dayOfWeek != DayOfWeek.MONDAY) {
            result = result.minus(1, DateTimeUnit.DAY)
        }
        return result
    }

/**
 * Extension function that jumps to the first day of the month.
 *
 * @return [LocalDate] representing the first day of the month
 */
internal val LocalDate.startOfMonth: LocalDate
    get() = LocalDate(this.year, this.month, 1)

/**
 * Extension function that checks if year is a leap year.
 *
 * Example: 2016, 2020, 2024, etc
 *
 * @return [Boolean] whether year is a leap year
 */
internal val LocalDate.isLeapYear: Boolean
    get() {
        var isLeapYear = year % 4 == 0
        isLeapYear = isLeapYear && (year % 100 != 0 || year % 400 == 0)

        return isLeapYear
    }

/**
 * Extension function that gets the maximum days of month
 *
 * @return [Int] of days in month
 */
internal fun Month.length(leapYear: Boolean): Int = when (this) {
    Month.JANUARY -> 31
    Month.FEBRUARY -> {
        if (leapYear) {
            29
        } else {
            28
        }
    }
    Month.MARCH -> 31
    Month.APRIL -> 30
    Month.MAY -> 31
    Month.JUNE -> 30
    Month.JULY -> 31
    Month.AUGUST -> 31
    Month.SEPTEMBER -> 30
    Month.OCTOBER -> 31
    Month.NOVEMBER -> 30
    Month.DECEMBER -> 31
    else -> 30
}

/**
 * Extension function that gets the maximum days of month
 *
 * @return [Int] of days in month
 */
internal val LocalDate.lengthOfMonth: Int
    get() = month.length(isLeapYear)

/**
Extension function that jumps to the last day of the month.
@return [LocalDate] representing the last day of the month
 */
internal val LocalDate.endOfMonth: LocalDate
    get() {
        return LocalDate(this.year, this.month, lengthOfMonth)
    }

/**
 * Get the first day of the previous week from the current date.
 *
 * Skips in current week to previous month's Monday if day is the first day of the month and not Monday.
 * Skips to previous week if the current day of the month is greater than or equal to 7 or is the first day of the month and is Monday.
 * Skips to the first day of the previous month otherwise.
 * @return The first day of the previous week as a `LocalDate` object.
 */
internal val LocalDate.previousWeek: LocalDate
    get() = when {
        dayOfMonth >= Constants.DAYS_IN_WEEK ||
                dayOfMonth == Constants.FIRST_DAY_IN_MONTH
                && dayOfWeek == DayOfWeek.MONDAY -> minus(1, DateTimeUnit.WEEK)

        else -> startOfMonth
    }


/**
 * Get the date of the next week from the current date.
 *
 * The next week is determined based on the current day of the month and the remaining days in the month.
 * If the current day of the month is the first day of the month, it skips to the next Monday.
 * If there are less than 7 days remaining in the current month, it skips to the first day of the next month.
 * @return The first day of the next week as a `LocalDate` object.
 */
internal val LocalDate.nextWeek: LocalDate
    get() = when {
        dayOfMonth == Constants.FIRST_DAY_IN_MONTH -> plus((7 - dayOfWeek.isoDayNumber) + 1, DateTimeUnit.DAY)
        lengthOfMonth - dayOfMonth >= Constants.DAYS_IN_WEEK -> plus(1, DateTimeUnit.WEEK)
        else -> plus(1, DateTimeUnit.MONTH).startOfMonth
    }

/**
 * Returns a new `LocalDate` instance representing the previous date based on the `CalendarConfig` passed.
 *
 * If `CalendarConfig.style` is set to `CalendarStyle.MONTH`, the function returns the first day of the previous month.
 * If `CalendarConfig.style` is set to `CalendarStyle.WEEK`, the function returns the first day (Monday) of the previous week.
 *
 * @param config The `CalendarConfig` to determine the jump step.
 * @return A new `LocalDate` instance representing the previous date based on the `CalendarConfig`.
 */
fun LocalDate.jumpPrev(config: CalendarConfig): LocalDate = when (config.style) {
    CalendarStyle.MONTH -> this.minus(1, DateTimeUnit.MONTH).startOfMonth
    CalendarStyle.WEEK -> this.previousWeek
}

/**
 * Returns a new `LocalDate` instance representing the next date based on the `CalendarConfig` passed.
 *
 * If `CalendarConfig.style` is set to `CalendarStyle.MONTH`, the function returns the first day of the next month.
 * If `CalendarConfig.style` is set to `CalendarStyle.WEEK`, the function returns the first day (Monday) of the next week.
 *
 * @param config The `CalendarConfig` to determine the jump step.
 * @return A new `LocalDate` instance representing the next date based on the `CalendarConfig`.
 */
fun LocalDate.jumpNext(config: CalendarConfig): LocalDate = when (config.style) {
    CalendarStyle.MONTH -> this.plus(1, DateTimeUnit.MONTH).startOfMonth
    CalendarStyle.WEEK -> this.nextWeek
}

/**
 * Returns the initial date to be displayed on the CalendarView based on the selection mode.
 * @param selection The selection mode.
 * @param boundary The boundary of the calendar.
 * @return The initial date to be displayed on the CalendarView.
 */
internal fun getInitialCameraDate(
    selection: CalendarSelection,
    boundary: ClosedRange<LocalDate>
): LocalDate {
    val cameraDateBasedOnMode = when (selection) {
        is CalendarSelection.Date -> selection.selectedDate
        is CalendarSelection.Dates -> selection.selectedDates?.firstOrNull()
        is CalendarSelection.Period -> selection.selectedRange?.start
    } ?: run {
        val now = LocalDate.now()
        if (now in boundary) now else boundary.start
    }
    return cameraDateBasedOnMode.startOfWeekOrMonth
}

/**
 * Returns the custom initial date in case the camera date is within the boundary. Otherwise, it returns null.
 *
 * @param cameraDate The initial camera date.
 * @param boundary The boundary of the calendar.
 * @return The initial camera date if it's within the boundary, otherwise null.
 */
internal fun getInitialCustomCameraDate(
    cameraDate: LocalDate?,
    boundary: ClosedRange<LocalDate>
): LocalDate? = cameraDate?.takeIf { it in boundary }?.startOfWeekOrMonth

/**
 * Get selection value of date.
 */
internal val CalendarSelection.dateValue: LocalDate?
    get() = if (this is CalendarSelection.Date) selectedDate else null

/**
 * Get selection value of dates.
 */
internal val CalendarSelection.datesValue: Array<LocalDate>
    get() {
        val value = if (this is CalendarSelection.Dates) selectedDates?.toMutableList()
            ?: emptyList() else emptyList()
        return value.toTypedArray()
    }

/**
 * Get selection value of range.
 */
internal val CalendarSelection.rangeValue: Array<LocalDate?>
    get() {
        val value = if (this is CalendarSelection.Period) selectedRange else null
        return mutableListOf(value?.start, value?.endInclusive).toTypedArray()
    }

/**
 * Get range start value.
 */
internal val List<LocalDate?>.startValue: LocalDate?
    get() = this.getOrNull(Constants.RANGE_START)

/**
 * Get range end value.
 */
internal val List<LocalDate?>.endValue: LocalDate?
    get() = this.getOrNull(Constants.RANGE_END)

/**
 * Calculate the month data based on the camera date and the restrictions.
 */
internal fun calcMonthData(
    config: CalendarConfig,
    cameraDate: LocalDate,
    today: LocalDate = LocalDate.now()
): CalendarMonthData {
    val months = Month.entries.toMutableList()

    // Check that months are within the boundary
    val boundaryFilteredMonths = months.filter { month ->
        val maxDayOfMonth = month.length(cameraDate.isLeapYear)
        val startDay = minOf(config.boundary.start.dayOfMonth, maxDayOfMonth)
        val endDay = minOf(config.boundary.endInclusive.dayOfMonth, maxDayOfMonth)
        val cameraDateWithMonth = LocalDate(cameraDate.year, month, startDay)
        cameraDateWithMonth in config.boundary || LocalDate(cameraDateWithMonth.year, cameraDateWithMonth.month, endDay) in config.boundary
    }

    return CalendarMonthData(
        selected = cameraDate.month,
        thisMonth = today.month,
        disabled = months.minus(boundaryFilteredMonths.toSet()),
    )
}

private fun LocalDate.isoWeekNumber(): Int {
    if (firstWeekInYearStart(year + 1) < this) return 1

    val currentYearStart = firstWeekInYearStart(year)
    val start = if (this < currentYearStart) firstWeekInYearStart(year - 1) else currentYearStart

    val currentCalendarWeek = start.until(this, DateTimeUnit.WEEK) + 1

    return currentCalendarWeek
}

private fun firstWeekInYearStart(year: Int): LocalDate {
    val jan1st = LocalDate(year, 1, 1)
    val previousMonday = jan1st.minus(jan1st.dayOfWeek.ordinal, DateTimeUnit.DAY)

    return if (jan1st.dayOfWeek <= DayOfWeek.THURSDAY) previousMonday else previousMonday.plus(1, DateTimeUnit.WEEK)
}

/**
 * Calculate the calendar data based on the camera-date.
 */
internal fun calcCalendarData(config: CalendarConfig, cameraDate: LocalDate): CalendarData {
    var weekCameraDate = cameraDate

    val firstDayOfWeek = DayOfWeek.MONDAY
    val dayOfWeek = cameraDate.dayOfWeek
    val diff = (dayOfWeek.isoDayNumber - firstDayOfWeek.isoDayNumber + 7) % 7

    val offsetStart = when (config.style) {
        CalendarStyle.MONTH -> diff
        CalendarStyle.WEEK -> {
            // Calculate the difference in days to the first day of the week from the camera date
            val dayOfWeekInMonth = cameraDate.startOfMonth.dayOfWeek
            val dayDiff = (dayOfWeekInMonth.isoDayNumber - firstDayOfWeek.isoDayNumber + 7) % 7

            // Adjust weekCameraDate to the start of the week if necessary
            val adjustedWeekCameraDate =
                if (cameraDate.dayOfMonth <= Constants.DAYS_IN_WEEK && dayDiff > 0) {
                    cameraDate.minus((cameraDate.dayOfWeek.isoDayNumber - firstDayOfWeek.isoDayNumber + 7) % 7, DateTimeUnit.DAY)
                } else {
                    weekCameraDate
                }

            // Calculate the offset based on the adjustedWeekCameraDate
            ((adjustedWeekCameraDate.dayOfWeek.isoDayNumber - firstDayOfWeek.isoDayNumber + 7) % 7).also {
                // Update weekCameraDate if it was adjusted
                weekCameraDate = adjustedWeekCameraDate
            }
        }
    }

    val days = when (config.style) {
        CalendarStyle.MONTH -> cameraDate.lengthOfMonth
        CalendarStyle.WEEK -> DayOfWeek.entries.size
    }

    val rangedDays = (1..days.plus(offsetStart)).toMutableList()
        .map { dayIndex ->
            val date = when (config.style) {
                CalendarStyle.MONTH -> cameraDate
                    .startOfMonth
                    .plus(dayIndex.minus(1), DateTimeUnit.DAY)
                    .minus(offsetStart, DateTimeUnit.DAY)

                CalendarStyle.WEEK -> weekCameraDate
                    .plus(dayIndex.minus(1), DateTimeUnit.DAY)
                    .minus(offsetStart, DateTimeUnit.DAY)
            }
            Pair(CalendarViewType.DAY, date)
        }.drop(
            when (config.style) {
                CalendarStyle.MONTH -> offsetStart
                CalendarStyle.WEEK -> 0
            }
        ).dropLast(
            when (config.style) {
                CalendarStyle.MONTH -> 0
                CalendarStyle.WEEK -> offsetStart
            }
        ).toMutableList()

    val isMonthStartOffset = weekCameraDate.dayOfMonth <= 7
    if (isMonthStartOffset) {
        repeat(offsetStart) {
            rangedDays.add(0, Pair(CalendarViewType.DAY_START_OFFSET, LocalDate.now()))
        }
    }

    val chunkedDays: List<List<Pair<CalendarViewType, Any>>> =
        rangedDays.chunked(Constants.DAYS_IN_WEEK)

    val weekDays: List<List<Pair<CalendarViewType, Any>>> = chunkedDays.map { week ->
        if (config.displayCalendarWeeks) {
            val newWeek = week.toMutableList().apply {
                val firstDateCalendarWeek =
                    week.first { it.first == CalendarViewType.DAY }.second as LocalDate
                add(0, Pair(CalendarViewType.CW, firstDateCalendarWeek.isoWeekNumber()))
            }
            newWeek
        } else week
    }

    return CalendarData(
        offsetStart = offsetStart,
        weekCameraDate = weekCameraDate,
        cameraDate = cameraDate,
        days = weekDays,
    )
}

internal fun LocalDate.isAfter(other: LocalDate): Boolean {
    return this.toEpochDays() > other.toEpochDays()
}

internal fun LocalDate.isBefore(other: LocalDate): Boolean {
    return other.isAfter(this)
}

/**
 * Calculate the calendar date-data based on the date.
 */
internal fun calcCalendarDateData(
    date: LocalDate,
    calendarViewData: CalendarData,
    selection: CalendarSelection,
    config: CalendarConfig,
    selectedDate: LocalDate?,
    selectedDates: List<LocalDate>?,
    selectedRange: Pair<LocalDate?, LocalDate?>
): CalendarDateData? {

    if (date.monthNumber != calendarViewData.cameraDate.monthNumber) return null

    var selectedStartInit = false
    var selectedEnd = false
    var selectedBetween = false
    val selected = when (selection) {
        is CalendarSelection.Date -> selectedDate == date
        is CalendarSelection.Dates -> {
            selectedDates?.contains(date) ?: false
        }

        is CalendarSelection.Period -> {
            val selectedStart = selectedRange.first == date
            selectedStartInit = selectedStart && selectedRange.second != null
            selectedEnd = selectedRange.second == date
            selectedBetween = (selectedRange.first?.let { date.isAfter(it) } ?: false)
                    && selectedRange.second?.let { date.isBefore(it) } ?: false
            selectedBetween || selectedStart || selectedEnd
        }
    }
    val outOfBoundary = date !in config.boundary
    val disabledDate = config.disabledDates?.contains(date) ?: false

    return CalendarDateData(
        date = date,
        disabled = disabledDate,
        disabledPassively = outOfBoundary,
        selected = selected,
        selectedBetween = selectedBetween,
        selectedStart = selectedStartInit,
        selectedEnd = selectedEnd,
    )
}



