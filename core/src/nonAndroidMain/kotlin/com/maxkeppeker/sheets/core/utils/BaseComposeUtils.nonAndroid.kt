package com.maxkeppeker.sheets.core.utils

import androidx.compose.runtime.Composable

/**
 * Determines whether the current screen should use landscape mode.
 *
 * @return `true` if the screen height is less than the [TABLET_THRESHOLD] in landscape mode, `false` otherwise.
 */
@Composable
actual fun shouldUseLandscape(): Boolean {
    return false
}

@Composable
actual fun isLandscape(): Boolean {
    return false
}