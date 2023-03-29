package ua.blackwind.limbushelper.ui.screens.filter_screen.state

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.util.*

data class FilterDrawerSheetState(
    val filterSheetMode: FilterSheetMode,
    val skillState: FilterSkillBlockState,
    val resistState: FilterDamageStateBundle,
    val effectsState: FilterEffectBlockState,
)

sealed class FilterSheetMode {
    object Type: FilterSheetMode()
    object Effects: FilterSheetMode()
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

data class FilterResistButtonLabels(
    val ineffective: String,
    val normal: String,
    val fatal: String
)

sealed class SelectedButtonPostion {
    object None: SelectedButtonPostion()
    object First: SelectedButtonPostion()
    object Second: SelectedButtonPostion()
    object Third: SelectedButtonPostion()
}

