package ua.blackwind.limbushelper.domain.party.model

import ua.blackwind.limbushelper.domain.sinner.model.Identity

data class PartyIdentity(
    val identity: Identity,
    val isActive: Boolean
)