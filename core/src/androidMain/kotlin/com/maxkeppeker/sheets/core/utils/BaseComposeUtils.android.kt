package com.maxkeppeker.sheets.core.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Determines whether the current screen should use landscape mode.
 *
 * @return `true` if the screen height is less than the [TABLET_THRESHOLD] in landscape mode, `false` otherwise.
 */
@Composable
actual fun shouldUseLandscape(): Boolean {
    return LocalConfiguration.current.screenHeightDp < TABLET_THRESHOLD
}

@Composable
actual fun isLandscape(): Boolean {
    return LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
}