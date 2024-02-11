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
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.mk.sheets.compose.samples

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.input.InputDialog
import com.maxkeppeler.sheets.input.models.InputCheckboxGroup
import com.maxkeppeler.sheets.input.models.InputHeader
import com.maxkeppeler.sheets.input.models.InputSelection

@Composable
internal fun InputSample2(closeSelection: () -> Unit) {

    val inputOptions = listOf(
        InputCheckboxGroup(
            header = InputHeader(
                title = "What are your favorite fruits?",
                body = "Select only fruits that you eat regularly.",
            ),
            items = listOf("Banana", "Mango", "Oranges", "Pineapple"),
            required = true,
            key = "Fruits"
        ),
    )

    InputDialog(
        state = rememberUseCaseState(visible = true, onCloseRequest = { closeSelection() }),
        selection = InputSelection(
            input = inputOptions,
            onPositiveClick = { result ->
                val selectionIndex = result.getIntArray("Fruits") // or index 0 if no key was set
                // Handle selection
            },
        )
    )
}