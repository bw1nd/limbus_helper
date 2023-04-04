package ua.blackwind.limbushelper.domain.sinner.model

import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.RiskLevel
import ua.blackwind.limbushelper.domain.common.Sin

data class Ego(
    val id: Int,
    val name: String,
    val sinnerId: Int,
    val risk: RiskLevel,
    val awakeningSkill: EgoSkill,
    val corrosionSkill: EgoSkill?,
    val passive: Passive,
    val resourceCost: Map<Sin, Int>,
    val sanityCost: Int,
    val sinResistances: Map<Sin, EgoSinResistType>,
    val imageUrl: String
)
