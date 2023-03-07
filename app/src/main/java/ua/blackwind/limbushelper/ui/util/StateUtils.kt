package ua.blackwind.limbushelper.ui.util

import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Sin

/**
 * State holder representing single Damage or Sin value
 */
sealed class StateType<out T: Any> {
    object Empty: StateType<Nothing>()
    data class Value<T: Any>(val value: T): StateType<T>()
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

sealed class FilterDrawerState() {
    object Open: FilterDrawerState()
    object Closed: FilterDrawerState()
}
