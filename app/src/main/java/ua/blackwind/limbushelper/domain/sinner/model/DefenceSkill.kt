package ua.blackwind.limbushelper.domain.sinner.model

import ua.blackwind.limbushelper.domain.common.Sin

data class DefenceSkill(
    val id: Int,
    val name: String,
    val type: DefenceSkillType,
    val skillPower: Int,
    val coinPower: Int,
    val coinCount: Int,
    val sin: Sin?,
)

enum class DefenceSkillType {
    GUARD,
    EVADE,
    COUNTER
}