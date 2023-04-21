package ua.blackwind.limbushelper.domain.sinner.model

import ua.blackwind.limbushelper.domain.common.Sin

data class DefenseSkill(
    val id: Int,
    val name: String,
    val type: DefenseSkillType,
    val basePower: Int,
    val coinPower: Int,
    val coinCount: Int,
    val sin: Sin?,
)

enum class DefenseSkillType {
    GUARD,
    EVADE,
    COUNTER
}