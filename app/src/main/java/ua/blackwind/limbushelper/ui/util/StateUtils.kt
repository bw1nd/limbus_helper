package ua.blackwind.limbushelper.ui.util

import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.usecase.FilterDamageTypeArg
import ua.blackwind.limbushelper.domain.sinner.usecase.FilterSinTypeArg

/**
 * State holder representing single Damage or Sin value
 */
sealed class StateType<out T: Any> {
    object Empty: StateType<Nothing>()
    data class Value<T: Any>(val value: T): StateType<T>()
}

fun StateType<DamageType>.toFilterDamageTypeArg() =
    when (this) {
        StateType.Empty -> FilterDamageTypeArg.Empty
        is StateType.Value -> FilterDamageTypeArg.Type(this.value)
    }

fun StateType<Sin>.toFilterSinTypeArg() =
    when (this) {
        StateType.Empty -> FilterSinTypeArg.Empty
        is StateType.Value -> FilterSinTypeArg.Type(this.value)
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

data class FilterResistButtonLabels(
    val ineffective: String,
    val normal: String,
    val fatal: String
)

sealed class FilterSheetMode {
    object Type: FilterSheetMode()
    object Effects: FilterSheetMode()
}

data class FilterEffectBlockState(
    val effects: Map<Effect, Boolean>
)
