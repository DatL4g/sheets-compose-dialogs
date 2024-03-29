package com.maxkeppeler.sheets.calendar.models

enum class FormatLocale {
    Chinese,
    Japanese,
    Default;

    companion object
}

expect fun FormatLocale.Companion.getDefault(): FormatLocale