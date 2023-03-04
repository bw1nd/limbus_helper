package com.example.limbushelper.domain.sinner.model

import com.example.limbushelper.domain.IdentityDamageResistType

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
    val speed: Pair<Int, Int>,
    val firstSkillId: Int,
    val secondSkillId: Int,
    val thirdSkillId: Int,
    val passiveId: Int,
    val supportId: Int,
    val imageUrl: String
)
