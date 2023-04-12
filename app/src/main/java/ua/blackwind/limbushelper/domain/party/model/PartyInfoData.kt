package ua.blackwind.limbushelper.domain.party.model

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.InfoPanelDamageResist

data class PartyInfoData(
    val skillCountByDamage: Map<DamageType, Int>,
    val skillCountBySin: Map<Sin, Int>,
    val resistByDamagePotency: Map<DamageType, InfoPanelDamageResist>,
    val resistBySinPotency: Map<Sin, InfoPanelDamageResist>,
    val activeIdentityCount: Int,
    val totalIdentityCount: Int
)
