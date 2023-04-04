package ua.blackwind.limbushelper.domain.filter

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.Sin

data class IdentityFilter(
    val resist: FilterResistSetArg,
    val skills: IdentityFilterSkillsSetArg,
    val effects: List<Effect>,
    val sinners: List<Int>
)

fun IdentityFilter.isEmpty() =
    resist.isEmpty() && skills.isEmpty() && effects.isEmpty() && sinners.isEmpty()


data class EgoFilter(
    val skillFilterArg: FilterSkillArg,
    val resistSetArg: EgoFilterSinResistTypeArg,
    val priceSetArg: EgoFilterPriceSetArg,
    val effects: List<Effect>,
    val sinners: List<Int>
)

data class EgoFilterSinResistTypeArg(
    val resistList: List<Pair<EgoSinResistType, Sin>>
)

data class EgoFilterPriceSetArg(
    val priceList: List<Sin>
)

data class FilterResistSetArg(
    val ineffective: FilterDamageTypeArg,
    val normal: FilterDamageTypeArg,
    val fatal: FilterDamageTypeArg
)

fun FilterResistSetArg.isEmpty() =
    this.ineffective == FilterDamageTypeArg.Empty
            && this.normal == FilterDamageTypeArg.Empty
            && this.fatal == FilterDamageTypeArg.Empty

data class IdentityFilterSkillsSetArg(
    val first: FilterSkillArg,
    val second: FilterSkillArg,
    val third: FilterSkillArg
)

fun IdentityFilterSkillsSetArg.toSkillList() = listOf(first, second, third)

fun IdentityFilterSkillsSetArg.isEmpty() =
    this.first.damageType == FilterDamageTypeArg.Empty
            && first.sin == FilterSinTypeArg.Empty
            && second.damageType == FilterDamageTypeArg.Empty
            && second.sin == FilterSinTypeArg.Empty
            && third.damageType == FilterDamageTypeArg.Empty
            && third.sin == FilterSinTypeArg.Empty

data class FilterSkillArg(
    val damageType: FilterDamageTypeArg,
    val sin: FilterSinTypeArg
)

fun FilterSkillArg.isStrict() = this.damageType !is FilterDamageTypeArg.Empty &&
        this.sin !is FilterSinTypeArg.Empty

sealed class FilterDamageTypeArg {
    object Empty: FilterDamageTypeArg()
    data class Type(
        val type: DamageType
    ): FilterDamageTypeArg()
}

fun FilterDamageTypeArg.toDamageType() = when (this) {
    is FilterDamageTypeArg.Empty -> null
    is FilterDamageTypeArg.Type -> this.type
}

sealed class FilterSinTypeArg {
    object Empty: FilterSinTypeArg()
    data class Type(
        val type: Sin
    ): FilterSinTypeArg()
}

fun FilterSinTypeArg.toSin() = when (this) {
    FilterSinTypeArg.Empty -> null
    is FilterSinTypeArg.Type -> this.type
}