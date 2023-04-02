package ua.blackwind.limbushelper.domain.party.model

const val DEFAULT_PARTY_ID = 1

data class Party(
    val id: Int,
    val name: String,
    val identityList: List<PartyIdentity>
)


