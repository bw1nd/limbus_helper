package ua.blackwind.limbushelper.ui.screens.party_building_screen.model

import ua.blackwind.limbushelper.domain.sinner.model.Identity


data class PartyIdentityItem(
    val identity: Identity,
    val isActive: Boolean
)
