package ua.blackwind.limbushelper.ui.screens.party_building_screen.model

import ua.blackwind.limbushelper.domain.sinner.model.Sinner

data class PartySinnerModel(
    val sinner: Sinner,
    val identities: List<PartyIdentityModel>
)
