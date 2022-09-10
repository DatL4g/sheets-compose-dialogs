@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package com.maxkeppeler.sheets.list.models

import androidx.annotation.IntRange
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.BaseSelection

/**
 * The selection configuration for the list dialog.
 * @param options The options that will be displayed.
 */
sealed class ListSelection(
    open val options: List<ListOption> = listOf()
) : BaseSelection() {

    /**
     * Single-choice selection for the list dialog.
     * @param options The options that will be displayed.
     * @param showRadioButtons Show the options with radio buttons.
     * @param withButtonView Show the dialog with the buttons view.
     * @param negativeButton The button that will be used as a negative button.
     * @param onNegativeClick The listener that is invoked when the negative button is clicked.
     * @param positiveButton The button that will be used as a positive button.
     * @param onSelectOption The listener that returns the selected index and the selected option when the positive button is clicked.
     */
    class Single(
        override val options: List<ListOption>,
        val showRadioButtons: Boolean = false,
        override val withButtonView: Boolean = true,
        override val negativeButton: SelectionButton? = null,
        override val onNegativeClick: (() -> Unit)? = null,
        override val positiveButton: SelectionButton? = null,
        val onSelectOption: (index: Int, option: ListOption) -> Unit
    ) : ListSelection()

    /**
     * Multiple-choice selection for the list dialog.
     * @param options The options that will be displayed.
     * @param showCheckBoxes Show the options with check boxes.
     * @param minChoices The minimum amount of choices that are allowed.
     * @param maxChoices The maximum amount of choices that are allowed.
     * @param negativeButton The button that will be used as a negative button.
     * @param onNegativeClick The listener that is invoked when the negative button is clicked.
     * @param positiveButton The button that will be used as a positive button.
     * @param onSelectOptions The listener that returns the selected indices and the selected options when the positive button is clicked.
     */
    class Multiple(
        override val options: List<ListOption>,
        val showCheckBoxes: Boolean = false,
        @IntRange(from = 1L, to = 90L) val minChoices: Int? = null,
        @IntRange(from = 3L, to = 90L) val maxChoices: Int? = null,
        override val negativeButton: SelectionButton? = null,
        override val onNegativeClick: (() -> Unit)? = null,
        override val positiveButton: SelectionButton? = null,
        val onSelectOptions: (selectedIndices: List<Int>, selectedOptions: List<ListOption>) -> Unit
    ) : ListSelection()
}