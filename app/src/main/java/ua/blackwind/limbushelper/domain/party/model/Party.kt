package ua.blackwind.limbushelper.domain.party.model

data class Party(
    val id: Int,
    val identityList: List<Pair<Int, Boolean>>
)