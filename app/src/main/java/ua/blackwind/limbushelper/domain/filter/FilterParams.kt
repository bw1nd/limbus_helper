package ua.blackwind.limbushelper.domain.filter

import ua.blackwind.limbushelper.domain.common.*

data class IdentityFilter(
    val skills: IdentityFilterSkillsSetArg,
    val resist: FilterResistSetArg,
    val effects: List<Effect>,
    val sinners: List<Int>
)

fun IdentityFilter.isEmpty() =
    resist.isEmpty() && skills.isEmpty() && effects.isEmpty() && sinners.isEmpty()

data class EgoFilter(
    val skillFilterArg: FilterSkillArg,
    val resistSetArg: Map<EgoSinResistType, Sin>,
    val priceSetArg: List<Sin>,
    val effects: List<Effect>,
    val sinners: List<Int>
)

data class FilterResistSetArg(
    val ineffective: TypeHolder<DamageType>,
    val normal: TypeHolder<DamageType>,
    val fatal: TypeHolder<DamageType>
)

fun FilterResistSetArg.isEmpty() =
    this.ineffective == TypeHolder.Empty
            && this.normal == TypeHolder.Empty
            && this.fatal == TypeHolder.Empty

data class IdentityFilterSkillsSetArg(
    val first: FilterSkillArg,
    val second: FilterSkillArg,
    val third: FilterSkillArg
)

fun IdentityFilterSkillsSetArg.toSkillList() = listOf(first, second, third)

fun IdentityFilterSkillsSetArg.isEmpty() =
    first.isEmpty() && second.isEmpty() && third.isEmpty()

data class FilterSkillArg(
    val damageType: TypeHolder<DamageType>,
    val sin: TypeHolder<Sin>
)

fun FilterSkillArg.isEmpty() =
    damageType is TypeHolder.Empty && sin is TypeHolder.Empty

fun FilterSkillArg.isStrict() = this.damageType !is TypeHolder.Empty &&
        this.sin !is TypeHolder.Empty