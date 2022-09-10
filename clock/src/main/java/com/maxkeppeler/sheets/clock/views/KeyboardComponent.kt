package com.maxkeppeler.sheets.clock.views


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.maxkeppeler.sheets.clock.utils.Constants
import com.maxkeppeler.sheets.core.R as RC

/**
 * The keyboard component that is used to input the clock time.
 * @param keys A list of keys that will be displayed.
 * @param disabledKeys A list of the keys that are displayed.
 * @param onEnterValue The listener that is invoked when a value was clicked.
 * @param onPrevAction The listener that is invoked when [Constants.ACTION_PREV] was clicked.
 * @param onNextAction The listener that is invoked when [Constants.ACTION_NEXT] was clicked.
 */
@Composable
internal fun KeyboardComponent(
    keys: List<String>,
    disabledKeys: List<String>,
    onEnterValue: (Int) -> Unit,
    onPrevAction: () -> Unit,
    onNextAction: () -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(RC.dimen.scd_normal_150),
                vertical = dimensionResource(RC.dimen.scd_normal_100)
            ),
        columns = GridCells.Fixed(Constants.KEYBOARD_COLUMNS),
        userScrollEnabled = false
    ) {
        items(keys) { key ->
            val disabled = disabledKeys.contains(key)
            KeyItemComponent(
                key = key,
                disabled = disabled,
                onNextAction = onNextAction,
                onPrevAction = onPrevAction,
                onEnterValue = onEnterValue
            )
        }
    }
}