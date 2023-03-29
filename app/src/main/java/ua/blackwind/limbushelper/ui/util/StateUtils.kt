package ua.blackwind.limbushelper.ui.util

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.filter.FilterDamageTypeArg
import ua.blackwind.limbushelper.domain.filter.FilterSinTypeArg

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



