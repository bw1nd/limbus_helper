package ua.blackwind.limbushelper.ui.screens.filter_screen.state

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.util.StateType

private const val FIRST_SINNER_ID = 1
private const val LAST_SINNER_ID = 12


sealed interface FilterDrawerSheetState {
    data class IdentityMode(
        val skillState: FilterSkillBlockState,
        val resistState: FilterDamageStateBundle,
        val effectsState: FilterEffectBlockState,
        val sinnersState: FilterSinnersBlockState
    ): FilterDrawerSheetState {
        companion object {
            fun getDefaultState() =
                IdentityMode(
                    emptyFilterSkillBlockState(),
                    emptyFilterResistClockState(),
                    emptyFilterEffectBlockState(),
                    emptyFilterSinnerBlockState()
                )
        }
    }

    data class EgoMode(
        val skillState: EgoFilterSkillBlockState,
        val resistState: EgoFilterResistBlockState,
        val effectsState: FilterEffectBlockState,
        val sinnersState: FilterSinnersBlockState
    ): FilterDrawerSheetState
}

data class EgoFilterSkillBlockState(
    val damageType: StateType<DamageType>,
    val sinType: StateType<Sin>
)

data class EgoFilterResistBlockState(
    val first: EgoFilterResistArg,
    val second: EgoFilterResistArg,
    val third: EgoFilterResistArg,
    val fourth: EgoFilterResistArg
)

data class EgoFilterResistArg(
    val sin: StateType<Sin>,
    val resist: StateType<EgoSinResistType>
)

data class EgoResistButtonLabels(
    val normal: String,
    val endure: String,
    val ineffective: String,
    val fatal: String
)

data class FilterDrawerSheetMethods(
    val onSwitchChange: (Int) -> Unit,
    val onFilterButtonClick: () -> Unit,
    val onClearFilterButtonPress: () -> Unit,
    val onSkillButtonClick: (SelectedSkillButtonPosition) -> Unit,
    val onSkillButtonLongPress: (SelectedSkillButtonPosition) -> Unit,
    val onSinPickerClick: (StateType<Sin>) -> Unit,
    val onResistButtonClick: (SelectedSkillButtonPosition) -> Unit,
    val onResistButtonLongPress: (SelectedResistButtonPosition) -> Unit,
    val onEffectCheckedChange: (Boolean, Effect) -> Unit,
    val onSinnerCheckedChange: (FilterSinnerModel) -> Unit
)

sealed class FilterSheetTab(val index: Int) {
    object Type: FilterSheetTab(0)
    object Effects: FilterSheetTab(1)
    object Sinners: FilterSheetTab(2)
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

sealed class SelectedSkillButtonPosition {
    object None: SelectedSkillButtonPosition()
    object First: SelectedSkillButtonPosition()
    object Second: SelectedSkillButtonPosition()
    object Third: SelectedSkillButtonPosition()
}

sealed class SelectedResistButtonPosition {
    object None: SelectedResistButtonPosition()
    object First: SelectedResistButtonPosition()
    object Second: SelectedResistButtonPosition()
    object Third: SelectedResistButtonPosition()
    object Fourth: SelectedResistButtonPosition()
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

