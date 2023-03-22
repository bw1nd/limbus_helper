package ua.blackwind.limbushelper.ui.screens.party_building_screen.model


data class PartyBuildingInfoPanelState(
    val attackByDamage: AttackByDamageInfo,
    val attackBySin: AttackBySinInfo,
    val defenceByDamage: DefenceByDamageInfo
)

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

enum class InfoPanelDamageResist {
    NA,
    Bad,
    Poor,
    Normal,
    Good,
    Perfect
}