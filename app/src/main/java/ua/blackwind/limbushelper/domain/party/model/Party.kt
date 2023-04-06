package ua.blackwind.limbushelper.domain.party.model

import ua.blackwind.limbushelper.domain.sinner.model.Ego

const val DEFAULT_PARTY_ID = 1

data class Party(
    val id: Int,
    val name: String,
    val identityList: List<PartyIdentity>,
    val egoList: List<Ego>
)


