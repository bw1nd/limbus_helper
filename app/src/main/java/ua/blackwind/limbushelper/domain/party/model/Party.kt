package ua.blackwind.limbushelper.domain.party.model

const val DEFAULT_PARTY_ID = 1

data class Party(
    val id: Int,
    val name: String,
    val sinners: List<PartySinner>
)
fun Party.getAllIdentities() = sinners.map { it.identities }.flatten().map { it.identity }

fun Party.getAllActiveIdentities() = sinners.map { it.identities }.flatten().filter { it.isActive }

fun Party.getAllEgo() = sinners.map { it.ego }.flatten()