package ua.blackwind.limbushelper.ui.screens.party_building_screen.model

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.party.model.PartyInfoData


data class PartyBuildingInfoPanelState(
    val attackByDamage: AttackByDamageInfo,
    val attackBySin: AttackBySinInfo,
    val defenceByDamage: DefenceByDamageInfo,
    val resistBySin: ResistBySinInfo,
    val activeIdentityCount: Int,
    val totalIdentityCount: Int
) {
    companion object {
        fun fromInfoData(data: PartyInfoData): PartyBuildingInfoPanelState {
            return PartyBuildingInfoPanelState(
                attackByDamage = AttackByDamageInfo(
                    slash = data.skillCountByDamage[DamageType.SLASH]!!,
                    pierce = data.skillCountByDamage[DamageType.PIERCE]!!,
                    blunt = data.skillCountByDamage[DamageType.BLUNT]!!,
                ),
                attackBySin = AttackBySinInfo(
                    wrath = data.skillCountBySin[Sin.WRATH]!!,
                    lust = data.skillCountBySin[Sin.LUST]!!,
                    sloth = data.skillCountBySin[Sin.SLOTH]!!,
                    gluttony = data.skillCountBySin[Sin.GLUTTONY]!!,
                    gloom = data.skillCountBySin[Sin.GLOOM]!!,
                    pride = data.skillCountBySin[Sin.PRIDE]!!,
                    envy = data.skillCountBySin[Sin.ENVY]!!,
                ),
                defenceByDamage = DefenceByDamageInfo(
                    slash = data.resistByDamagePotency[DamageType.SLASH]!!,
                    pierce = data.resistByDamagePotency[DamageType.PIERCE]!!,
                    blunt = data.resistByDamagePotency[DamageType.BLUNT]!!,
                ),
                resistBySin = ResistBySinInfo(
                    wrath = data.resistBySinPotency[Sin.WRATH]!!,
                    lust = data.resistBySinPotency[Sin.LUST]!!,
                    sloth = data.resistBySinPotency[Sin.SLOTH]!!,
                    gluttony = data.resistBySinPotency[Sin.GLUTTONY]!!,
                    gloom = data.resistBySinPotency[Sin.GLOOM]!!,
                    pride = data.resistBySinPotency[Sin.PRIDE]!!,
                    envy = data.resistBySinPotency[Sin.ENVY]!!,
                ),
                activeIdentityCount = data.activeIdentityCount,
                totalIdentityCount = data.totalIdentityCount
            )
        }
    }
}

data class AttackByDamageInfo(
    val slash: Int,
    val pierce: Int,
    val blunt: Int
)

data class AttackBySinInfo(
    val wrath: Int,
    val lust: Int,
    val sloth: Int,
    val gluttony: Int,
    val gloom: Int,
    val pride: Int,
    val envy: Int
)

data class DefenceByDamageInfo(
    val slash: InfoPanelDamageResist,
    val pierce: InfoPanelDamageResist,
    val blunt: InfoPanelDamageResist
)

data class ResistBySinInfo(
    val wrath: InfoPanelDamageResist,
    val lust: InfoPanelDamageResist,
    val sloth: InfoPanelDamageResist,
    val gluttony: InfoPanelDamageResist,
    val gloom: InfoPanelDamageResist,
    val pride: InfoPanelDamageResist,
    val envy: InfoPanelDamageResist
)

enum class InfoPanelDamageResist {
    NA,
    Bad,
    Poor,
    Normal,
    Good,
    Perfect
}