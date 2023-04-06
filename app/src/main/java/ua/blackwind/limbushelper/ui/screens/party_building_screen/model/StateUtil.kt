package ua.blackwind.limbushelper.ui.screens.party_building_screen.model


data class PartyBuildingInfoPanelState(
    val attackByDamage: AttackByDamageInfo,
    val attackBySin: AttackBySinInfo,
    val defenceByDamage: DefenceByDamageInfo,
    val resistBySin: ResistBySinInfo,
    val activeIdentityCount: Int,
    val totalIdentityCount: Int
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