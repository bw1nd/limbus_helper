package com.example.limbushelper.domain.sinner.model

import com.example.limbushelper.domain.DamageType
import com.example.limbushelper.domain.Effect
import com.example.limbushelper.domain.Sin

data class Skill(
    val id: Int,
    val name: String,
    val identityId: Int,
    val dmgType: DamageType,
    val sin: Sin,
    val copiesCount: Int,
    val baseDie: Int,
    val coinBonus: Int,
    val coinCount: Int,
    val effects: List<Effect>
)
