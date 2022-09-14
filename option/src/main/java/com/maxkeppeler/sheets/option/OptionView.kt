/*
 *  Copyright (C) 2022. Maximilian Keppeler (https://www.maxkeppeler.com)
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
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.maxkeppeler.sheets.option

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.BaseBehaviors
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.utils.BaseModifiers.dynamicContentWrapOrMaxHeight
import com.maxkeppeker.sheets.core.views.ButtonsComponent
import com.maxkeppeker.sheets.core.views.base.FrameBase
import com.maxkeppeler.sheets.core.R
import com.maxkeppeler.sheets.option.models.Option
import com.maxkeppeler.sheets.option.models.OptionConfig
import com.maxkeppeler.sheets.option.models.OptionSelection
import com.maxkeppeler.sheets.option.views.OptionBoundsComponent
import com.maxkeppeler.sheets.option.views.OptionComponent

/**
 * Option view for the use-case to display a list or grid of options.
 * @param selection The selection configuration for the dialog view.
 * @param config The general configuration for the dialog view.
 * @param header The header to be displayed at the top of the dialog view.
 * @param onCancel Listener that is invoked when the use-case was canceled.
 */
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun OptionView(
    selection: OptionSelection,
    config: OptionConfig = OptionConfig(),
    header: Header? = null,
    onCancel: () -> Unit = {},
) {

    val coroutine = rememberCoroutineScope()
    val state = rememberSaveable(
        saver = OptionState.Saver(selection, config),
        init = { OptionState(selection, config) }
    )

    val processSelection: (Option) -> Unit = { option ->
        state.processSelection(option)
        BaseBehaviors.autoFinish(
            selection = selection,
            condition = state.valid,
            coroutine = coroutine,
            onSelection = state::onFinish,
            onFinished = onCancel,
            onDisableInput = state::disableInput
        )
    }

    FrameBase(
        header = header,
        // Override content padding, spacing is within the scrollable container for display mode GRID_HORIZONTAL
        horizontalContentPadding = PaddingValues(horizontal = 0.dp),
        content = {
            OptionBoundsComponent(
                selection = selection,
                selectedOptions = state.selectedOptions
            )
            OptionComponent(
                modifier = Modifier.dynamicContentWrapOrMaxHeight(this),
                config = config,
                options = state.options,
                inputDisabled = state.inputDisabled,
                onOptionChange = processSelection
            )
        },
        buttonsVisible = selection.withButtonView
    ) {
        ButtonsComponent(
            onPositiveValid = state.valid,
            selection = selection,
            onNegative = { selection.onNegativeClick?.invoke() },
            onPositive = state::onFinish,
            onCancel = onCancel
        )
    }
}





