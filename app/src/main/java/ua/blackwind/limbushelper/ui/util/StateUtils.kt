package ua.blackwind.limbushelper.ui.util

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.common.TypeHolder
import ua.blackwind.limbushelper.domain.filter.FilterDamageTypeArg
import ua.blackwind.limbushelper.domain.filter.FilterSinTypeArg

fun TypeHolder<DamageType>.toFilterDamageTypeArg() =
    when (this) {
        TypeHolder.Empty -> FilterDamageTypeArg.Empty
        is TypeHolder.Value -> FilterDamageTypeArg.Type(this.value)
    }

fun TypeHolder<Sin>.toFilterSinTypeArg() =
    when (this) {
        TypeHolder.Empty -> FilterSinTypeArg.Empty
        is TypeHolder.Value -> FilterSinTypeArg.Type(this.value)
    }



