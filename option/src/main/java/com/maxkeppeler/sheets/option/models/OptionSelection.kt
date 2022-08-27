@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package com.maxkeppeler.sheets.option.models

import androidx.annotation.IntRange
import com.maxkeppeker.sheets.core.models.base.BaseSelection

/**
 * Available selection modes and selection-based configurations.
 */
sealed class OptionSelection(
    open val options: List<Option>
) : BaseSelection() {

    /**
     * Select a single option.
     */
    class Single(
        override val options: List<Option>,
        override val withButtonView: Boolean = true,
        override val negativeButtonText: String? = null,
        override val onNegativeClick: (() -> Unit)? = null,
        override val positiveButtonText: String? = null,
        val onSelectOption: (index: Int, option: Option) -> Unit
    ) : OptionSelection(options = options)

    /**
     * Select multiple options.
     */
    class Multiple(
        override val options: List<Option>,
        @IntRange(from = 1L, to = 90L) val minChoices: Int? = null,
        @IntRange(from = 3L, to = 90L) val maxChoices: Int? = null,
        val maxChoicesStrict: Boolean = true,
        override val negativeButtonText: String? = null,
        override val onNegativeClick: (() -> Unit)? = null,
        override val positiveButtonText: String? = null,
        val onSelectOptions: (selectedIndices: List<Int>, selectedOptions: List<Option>) -> Unit
    ) : OptionSelection(options = options)
}