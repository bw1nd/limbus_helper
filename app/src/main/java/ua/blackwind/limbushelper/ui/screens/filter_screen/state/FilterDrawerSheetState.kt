package ua.blackwind.limbushelper.ui.screens.filter_screen.state

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.util.StateType

private const val FIRST_SINNER_ID = 1
private const val LAST_SINNER_ID = 12

data class FilterDrawerSheetState(
    val skillState: FilterSkillBlockState,
    val resistState: FilterDamageStateBundle,
    val effectsState: FilterEffectBlockState,
    val sinnersState: FilterSinnersBlockState
) {
    companion object {
        fun getDefaultState() =
            FilterDrawerSheetState(
                emptyFilterSkillBlockState(),
                emptyFilterResistClockState(),
                emptyFilterEffectBlockState(),
                emptyFilterSinnerBlockState()
            )
    }
}

data class FilterDrawerSheetMethods(
    val onSwitchChange: (Int) -> Unit,
    val onFilterButtonClick: () -> Unit,
    val onClearFilterButtonPress: () -> Unit,
    val onSkillButtonClick: (SelectedButtonPosition) -> Unit,
    val onSkillButtonLongPress: (SelectedButtonPosition) -> Unit,
    val onSinPickerClick: (StateType<Sin>) -> Unit,
    val onResistButtonClick: (SelectedButtonPosition) -> Unit,
    val onEffectCheckedChange: (Boolean, Effect) -> Unit,
    val onSinnerCheckedChange: (FilterSinnerModel) -> Unit
)

sealed class FilterSheetMode {
    object Type: FilterSheetMode()
    object Effects: FilterSheetMode()
    object Sinners: FilterSheetMode()
}

/**
 * State holder representing state of skill filter block.
 */
data class FilterSkillBlockState(
    val damage: FilterDamageStateBundle,
    val sin: FilterSinStateBundle
)

/**
 * State holder representing damage type state of skills or resistances
 * in order they appear on screen.
 */
data class FilterDamageStateBundle(
    val first: StateType<DamageType>,
    val second: StateType<DamageType>,
    val third: StateType<DamageType>,
)

fun FilterDamageStateBundle.isUnique() = run {
    val filtered = listOf(first, second, third).filter { it !is StateType.Empty }
    filtered.size == filtered.toSet().size
}

/**
 * State holder representing sin type state of skills
 * in order they appear on screen.
 */
data class FilterSinStateBundle(
    val first: StateType<Sin>,
    val second: StateType<Sin>,
    val third: StateType<Sin>,
)

data class FilterEffectBlockState(
    val effects: Map<Effect, Boolean>
)

data class FilterSinnersBlockState(
    val sinners: Map<FilterSinnerModel, Boolean>
)

data class FilterResistButtonLabels(
    val ineffective: String,
    val normal: String,
    val fatal: String
)

sealed class SelectedButtonPosition {
    object None: SelectedButtonPosition()
    object First: SelectedButtonPosition()
    object Second: SelectedButtonPosition()
    object Third: SelectedButtonPosition()
}

private fun emptyFilterSkillBlockState() = FilterSkillBlockState(
    FilterDamageStateBundle(StateType.Empty, StateType.Empty, StateType.Empty),
    FilterSinStateBundle(StateType.Empty, StateType.Empty, StateType.Empty)
)

private fun emptyFilterResistClockState() =
    FilterDamageStateBundle(StateType.Empty, StateType.Empty, StateType.Empty)

fun emptyFilterEffectBlockState() = FilterEffectBlockState(
    Effect.values().associateWith { false }
)

fun emptyFilterSinnerBlockState() = FilterSinnersBlockState(
    (FIRST_SINNER_ID..LAST_SINNER_ID).associate { index -> FilterSinnerModel(index) to false }
)

