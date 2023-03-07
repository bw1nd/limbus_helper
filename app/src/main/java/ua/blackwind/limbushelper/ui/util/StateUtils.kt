package ua.blackwind.limbushelper.ui.util

import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Sin

sealed class StateType<out T: Any> {
    object Empty: StateType<Nothing>()
    data class Value<T: Any>(val value: T): StateType<T>()
}

data class FilterSkillBlockState(
    val damage: FilterDamageStateBundle,
    val sin: FilterSinStateBundle
)

data class FilterDamageStateBundle(
    val first: StateType<DamageType>,
    val second: StateType<DamageType>,
    val third: StateType<DamageType>,
)

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
