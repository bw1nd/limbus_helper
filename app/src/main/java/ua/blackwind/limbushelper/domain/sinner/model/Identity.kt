package ua.blackwind.limbushelper.domain.sinner.model

import ua.blackwind.limbushelper.domain.common.IdentityDamageResistType

data class Identity(
    val id: Int,
    val name: String,
    val sinnerId: Int,
    val rarity: Int,
    val slashRes: IdentityDamageResistType,
    val pierceRes: IdentityDamageResistType,
    val bluntRes: IdentityDamageResistType,
    val hp: Int,
    val defense: Int,
    val offense: Int,
    val speed: Pair<Int, Int>,
    val firstSkill: Skill,
    val secondSkill: Skill,
    val thirdSkill: Skill,
    val defenseSkill: DefenseSkill,
    val passive: Passive,
    val support: Support,
    val imageUrl: String
)