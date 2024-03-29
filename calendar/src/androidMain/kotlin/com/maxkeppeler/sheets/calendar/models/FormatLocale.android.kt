package com.maxkeppeler.sheets.calendar.models

import java.util.*

private fun FormatLocale.Companion.fromLocale(locale: Locale = Locale.getDefault()): FormatLocale {
    return when {
        Locale.SIMPLIFIED_CHINESE.let { locale.language == it.language && locale.country == it.country } -> FormatLocale.Chinese
        Locale.JAPANESE.let { locale.language == it.language } -> FormatLocale.Japanese
        else -> FormatLocale.Default
    }
}

actual fun FormatLocale.Companion.getDefault(): FormatLocale {
    return FormatLocale.fromLocale()
}