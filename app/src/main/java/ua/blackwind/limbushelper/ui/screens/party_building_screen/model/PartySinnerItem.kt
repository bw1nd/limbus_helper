package ua.blackwind.limbushelper.ui.screens.party_building_screen.model

import ua.blackwind.limbushelper.domain.sinner.model.Sinner

data class PartySinnerItem(
    val sinner: Sinner,
    val identities: List<PartyIdentityItem>
)
