package ua.blackwind.limbushelper.domain.sinner.model

import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.Sin

data class Identity(
    val id: Int,
    val name: String,
    val sinnerId: Int,
    val rarity: Int,
    val slashRes: IdentityDamageResistType,
    val pierceRes: IdentityDamageResistType,
    val bluntRes: IdentityDamageResistType,
    val maxHp: Int,
    val maxArmor: Int,
    val maxDamage: Int,
    val speed: Pair<Int, Int>,
    val firstSkill: Skill,
    val secondSkill: Skill,
    val thirdSkill: Skill,
    val passive: Passive,
    val support: Support,
    val imageUrl: String
)

fun Identity.getDamageImprint(): List<DamageType> {
    return listOf(
        firstSkill.dmgType,
        secondSkill.dmgType,
        thirdSkill.dmgType
    )
}

fun Identity.getSinImprint(): List<Sin> {
    return listOf(
        firstSkill.sin,
        secondSkill.sin,
        thirdSkill.sin
    )
}

